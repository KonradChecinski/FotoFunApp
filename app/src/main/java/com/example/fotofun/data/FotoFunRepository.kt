package com.example.fotofun.data

import com.example.fotofun.data.entities.Setting
import kotlinx.coroutines.flow.Flow

interface FotoFunRepository {
    suspend fun getSettingById(settingId: Int): Setting?

    suspend fun getSettingValue(settingName: String): Long

    fun getSettings(): Flow<List<Setting>>?

    suspend fun addSetting(setting: Setting)

    suspend fun updateSetting(settingName: String, settingValue: Long)
}