package com.example.cryptomonitor.data.database

import androidx.room.*
import com.example.cryptomonitor.data.models.ApiConfig

@Dao
interface ApiConfigDao {
    @Query("SELECT * FROM apiconfig WHERE platform = :platform")
    suspend fun getConfigForPlatform(platform: String): ApiConfig?
    
    @Query("SELECT * FROM apiconfig")
    suspend fun getAllConfigs(): List<ApiConfig>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ApiConfig)
}
