package com.example.cryptomonitor.data.repository

import android.content.Context
import androidx.room.Room
import com.example.cryptomonitor.data.api.*
import com.example.cryptomonitor.data.database.AppDatabase
import com.example.cryptomonitor.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CryptoRepository(
    private val database: AppDatabase,
    private val binanceService: BinanceService,
    private val bitsoService: BitsoService,
    private val exchangeRateService: ExchangeRateService
) {
    private var exchangeRate: Double = 1.0
    private var lastRateUpdate: Long = 0

    private suspend fun updateExchangeRate() {
        val now = System.currentTimeMillis()
        if (now - lastRateUpdate > 3600000) { // Update rate every hour
            try {
                val response = exchangeRateService.getExchangeRates()
                exchangeRate = response.rates["MXN"] ?: 1.0
                lastRateUpdate = now
            } catch (e: Exception) {
                // Keep using old rate if update fails
            }
        }
    }

    suspend fun getAllAlerts(): List<CryptoAlert> = withContext(Dispatchers.IO) {
        database.alertDao().getAllAlerts()
    }

    suspend fun getAllApiConfigs(): List<ApiConfig> = withContext(Dispatchers.IO) {
        database.apiConfigDao().getAllConfigs()
    }

    suspend fun getUserSettings(): UserSettings = withContext(Dispatchers.IO) {
        database.settingsDao().getUserSettings() ?: UserSettings()
    }

    suspend fun saveUserSettings(settings: UserSettings) = withContext(Dispatchers.IO) {
        database.settingsDao().saveUserSettings(settings)
    }

    suspend fun getPrice(coinId: String, platform: String): CryptoPrice = withContext(Dispatchers.IO) {
        val config = database.apiConfigDao().getConfigForPlatform(platform)
            ?: throw IllegalStateException("No API config found for $platform")

        updateExchangeRate()
        
        when (platform.lowercase()) {
            "binance" -> try {
                val response = binanceService.getPrice(config.apiKey, "${coinId}USDT")
                CryptoPrice(
                    coinId = coinId,
                    platform = "Binance",
                    price = response.price.toDouble() * exchangeRate // Convert to MXN
                )
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> throw IllegalStateException("Invalid Binance API key")
                    429 -> throw IllegalStateException("Too many requests to Binance")
                    else -> throw e
                }
            }
            "bitso" -> try {
                val response = bitsoService.getPrice(config.apiKey, "${coinId.lowercase()}_mxn")
                CryptoPrice(
                    coinId = coinId,
                    platform = "Bitso",
                    price = response.payload.last.toDouble()
                )
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> throw IllegalStateException("Invalid Bitso API key")
                    429 -> throw IllegalStateException("Too many requests to Bitso")
                    else -> throw e
                }
            }
            else -> throw IllegalArgumentException("Unsupported platform: $platform")
        }
    }

    suspend fun saveAlert(alert: CryptoAlert) = withContext(Dispatchers.IO) {
        database.alertDao().insertAlert(alert)
    }

    suspend fun saveApiConfig(config: ApiConfig) = withContext(Dispatchers.IO) {
        database.apiConfigDao().insertConfig(config)
    }
}
