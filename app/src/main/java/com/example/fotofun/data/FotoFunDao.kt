package com.example.fotofun.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fotofun.data.entities.Course
import com.example.fotofun.data.entities.Setting
import kotlinx.coroutines.flow.Flow

@Dao
interface FotoFunDao {

    @Query("SELECT * FROM Setting WHERE settingId = :settingId")
    suspend fun getSettingById(settingId: Int): Setting?

    @Query("SELECT settingValue FROM Setting WHERE settingName = :settingName")
    suspend fun getSettingValue(settingName: String): Long

    @Query("SELECT * FROM Setting")
    fun getSettings(): Flow<List<Setting>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSetting(setting: Setting)

    @Query("UPDATE Setting SET settingValue = :settingValue WHERE settingName = :settingName")
    suspend fun updateSetting(settingName: String, settingValue: Long)
}