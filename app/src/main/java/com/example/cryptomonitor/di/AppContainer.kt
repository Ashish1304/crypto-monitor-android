package com.example.cryptomonitor.di

import android.content.Context
import androidx.room.Room
import com.example.cryptomonitor.data.api.BinanceService
import com.example.cryptomonitor.data.api.BitsoService
import com.example.cryptomonitor.data.database.AppDatabase
import com.example.cryptomonitor.data.repository.CryptoRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val applicationContext: Context) {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "crypto_monitor_db"
        ).build()
    }

    private val binanceRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.binance.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val bitsoRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.bitso.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val binanceService: BinanceService by lazy {
        binanceRetrofit.create(BinanceService::class.java)
    }

    private val bitsoService: BitsoService by lazy {
        bitsoRetrofit.create(BitsoService::class.java)
    }

    val repository: CryptoRepository by lazy {
        CryptoRepository(database, binanceService, bitsoService)
    }
}
