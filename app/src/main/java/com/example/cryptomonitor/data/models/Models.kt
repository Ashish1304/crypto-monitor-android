package com.example.cryptomonitor.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CryptoAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val coinId: String,
    val platform: String,
    val upperLimit: Double? = null,
    val lowerLimit: Double? = null,
    val percentageChange: Double? = null,
    val isEnabled: Boolean = true
)

@Entity
data class ApiConfig(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val platform: String,
    val apiKey: String,
    val apiSecret: String
)

@Entity
data class UserSettings(
    @PrimaryKey
    val id: Int = 1,
    val preferredCurrency: String = "MXN"
)

data class CryptoPrice(
    val coinId: String,
    val platform: String,
    val price: Double,
    val timestamp: Long = System.currentTimeMillis()
)
