package com.example.cryptomonitor

import android.app.Application
import com.example.cryptomonitor.di.AppContainer
import com.example.cryptomonitor.service.PriceMonitorWorker

class CryptoApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        PriceMonitorWorker.schedule(this)
    }
}
