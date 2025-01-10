package com.example.cryptomonitor.data.repository

import com.example.cryptomonitor.data.api.BinanceService
import com.example.cryptomonitor.data.api.BitsoService
import com.example.cryptomonitor.data.database.AppDatabase
import com.example.cryptomonitor.data.models.ApiConfig
import com.example.cryptomonitor.data.models.CryptoAlert
import com.example.cryptomonitor.data.models.CryptoPrice

class CryptoRepository(
    private val database: AppDatabase,
    private val binanceService: BinanceService,
    private val bitsoService: BitsoService
) {
    suspend fun getPrice(coinId: String, platform: String): CryptoPrice {
        val config = database.apiConfigDao().getConfigForPlatform(platform)
            ?: throw IllegalStateException("No API config found for $platform")
            
        return when (platform) {
            "binance" -> binanceService.getPrice(config.apiKey, coinId)
            "bitso" -> bitsoService.getPrice(config.apiKey, coinId)
            else -> throw IllegalArgumentException("Unsupported platform: $platform")
        }
    }
    
    suspend fun saveAlert(alert: CryptoAlert) {
        database.alertDao().insertAlert(alert)
    }
    
    suspend fun saveApiConfig(config: ApiConfig) {
        database.apiConfigDao().insertConfig(config)
    }
}
