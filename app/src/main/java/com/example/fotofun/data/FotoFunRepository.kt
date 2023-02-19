package com.example.fotofun.data

import androidx.lifecycle.LiveData
import com.example.fotofun.data.entities.Setting
import kotlinx.coroutines.flow.Flow

interface FotoFunRepository {
    suspend fun getSettingById(settingId: Int): Setting?

    suspend fun getSettingValue(settingName: String): Long

    fun getSettings(): LiveData<List<Setting>>

    fun getSettingsFlow(): Flow<List<Setting>>

    suspend fun checkIfSettingsEmpty(): Boolean

    suspend fun addSetting(setting: Setting)

    suspend fun updateSetting(settingName: String, settingValue: Long)

    suspend fun deleteTable()
}