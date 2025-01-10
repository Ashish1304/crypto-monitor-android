package com.example.cryptomonitor.data.database

import androidx.room.*
import com.example.cryptomonitor.data.models.UserSettings

@Dao
interface SettingsDao {
    @Query("SELECT * FROM UserSettings WHERE id = 1")
    suspend fun getUserSettings(): UserSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserSettings(settings: UserSettings)
}
