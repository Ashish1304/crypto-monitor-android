package com.example.cryptomonitor.data.database

import android.content.Context
import androidx.room.*
import com.example.cryptomonitor.data.models.ApiConfig
import com.example.cryptomonitor.data.models.CryptoAlert

@Database(entities = [CryptoAlert::class, ApiConfig::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alertDao(): AlertDao
    abstract fun apiConfigDao(): ApiConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crypto_monitor_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface AlertDao {
    @Query("SELECT * FROM cryptoalert")
    suspend fun getAllAlerts(): List<CryptoAlert>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: CryptoAlert)
    
    @Delete
    suspend fun deleteAlert(alert: CryptoAlert)
}

@Dao
interface ApiConfigDao {
    @Query("SELECT * FROM apiconfig WHERE platform = :platform")
    suspend fun getConfigForPlatform(platform: String): ApiConfig?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ApiConfig)
}
