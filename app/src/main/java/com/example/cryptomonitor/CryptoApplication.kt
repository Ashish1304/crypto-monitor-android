package com.example.cryptomonitor

import android.app.Application
import com.example.cryptomonitor.data.repository.CryptoRepository
import com.example.cryptomonitor.di.AppContainer
import com.example.cryptomonitor.service.PriceMonitorWorker

class CryptoApplication : Application() {
    lateinit var repository: CryptoRepository

    override fun onCreate() {
        super.onCreate()
        val container = AppContainer(this)
        repository = container.repository
        PriceMonitorWorker.schedule(this)
    }
}
