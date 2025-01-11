package com.example.cryptomonitor.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
@TypeConverters(Converters::class)
data class UserSettings(
    @PrimaryKey
    val id: Int = 1,
    val preferredCurrency: String = "MXN",
    val selectedCryptos: Set<String> = emptySet()
)

data class CryptoPrice(
    val coinId: String,
    val platform: String,
    val price: Double,
    val timestamp: Long = System.currentTimeMillis()
)

class Converters {
    @TypeConverter
    fun fromString(value: String): Set<String> {
        val listType = object : TypeToken<Set<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: Set<String>): String {
        return Gson().toJson(value)
    }
}
