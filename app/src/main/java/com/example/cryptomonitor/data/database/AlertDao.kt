package com.example.cryptomonitor.data.database

import androidx.room.*
import com.example.cryptomonitor.data.models.CryptoAlert

@Dao
interface AlertDao {
    @Query("SELECT * FROM cryptoalert")
    suspend fun getAllAlerts(): List<CryptoAlert>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: CryptoAlert)
    
    @Delete
    suspend fun deleteAlert(alert: CryptoAlert)
}
