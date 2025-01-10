package com.example.cryptomonitor.data.repository

import com.example.cryptomonitor.data.api.BinanceService
import com.example.cryptomonitor.data.api.BitsoService
import com.example.cryptomonitor.data.database.AppDatabase
import com.example.cryptomonitor.data.models.ApiConfig
import com.example.cryptomonitor.data.models.CryptoAlert
import com.example.cryptomonitor.data.models.CryptoPrice
import com.example.cryptomonitor.data.models.UserSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CryptoRepository(
    private val database: AppDatabase,
    private val binanceService: BinanceService,
    private val bitsoService: BitsoService
) {
    suspend fun getAllAlerts(): List<CryptoAlert> = withContext(Dispatchers.IO) {
        database.alertDao().getAllAlerts()
    }

    suspend fun getAllApiConfigs(): List<ApiConfig> = withContext(Dispatchers.IO) {
        database.apiConfigDao().getAllConfigs()
    }

    suspend fun saveAlert(alert: CryptoAlert) = withContext(Dispatchers.IO) {
        database.alertDao().insertAlert(alert)
    }

    suspend fun saveApiConfig(config: ApiConfig) = withContext(Dispatchers.IO) {
        database.apiConfigDao().insertConfig(config)
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
            
        return@withContext when (platform.lowercase()) {
            "binance" -> {
                val response = binanceService.getPrice(config.apiKey, "${coinId}USDT")
                CryptoPrice(
                    coinId = coinId,
                    platform = "Binance",
                    price = response.price.toDouble()
                )
            }
            "bitso" -> {
                val response = bitsoService.getPrice(config.apiKey, "${coinId.lowercase()}_mxn")
                CryptoPrice(
                    coinId = coinId,
                    platform = "Bitso",
                    price = response.payload.last.toDouble()
                )
            }
            else -> throw IllegalArgumentException("Unsupported platform: $platform")
        }
    }
}
