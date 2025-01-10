package com.example.cryptomonitor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptomonitor.data.models.ApiConfig
import com.example.cryptomonitor.data.models.CryptoAlert
import com.example.cryptomonitor.data.models.UserSettings

@Database(
    entities = [
        CryptoAlert::class,
        ApiConfig::class,
        UserSettings::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alertDao(): AlertDao
    abstract fun apiConfigDao(): ApiConfigDao
    abstract fun settingsDao(): SettingsDao
}
