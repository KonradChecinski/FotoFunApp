package com.example.fotofun.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fotofun.data.entities.Email
import com.example.fotofun.data.entities.Setting
import kotlinx.coroutines.flow.Flow

@Dao
interface FotoFunDao {

    @Query("SELECT * FROM Setting WHERE settingId = :settingId")
    suspend fun getSettingById(settingId: Int): Setting?

    @Query("SELECT settingValue FROM Setting WHERE settingName = :settingName")
    suspend fun getSettingValue(settingName: String): Long

    @Query("SELECT * FROM Setting")
    fun getSettings(): LiveData<List<Setting>>

    @Query("SELECT * FROM Setting")
    fun getSettingsFlow(): Flow<List<Setting>>

    @Query("SELECT (SELECT COUNT(*) FROM Setting LIMIT 1) == 0")
    suspend fun checkIfSettingsEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSetting(setting: Setting)

    @Query("UPDATE Setting SET settingValue = :settingValue WHERE settingName = :settingName")
    suspend fun updateSetting(settingName: String, settingValue: Long)

    @Query("DELETE FROM Setting")
    suspend fun deleteTable()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmail(email: Email)

    @Query("SELECT * FROM Email WHERE emailId = :emailId")
    suspend fun getEmailById(emailId: Int): Email?
}