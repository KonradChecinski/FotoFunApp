package com.example.fotofun.data

import androidx.lifecycle.LiveData
import com.example.fotofun.data.entities.Setting
import kotlinx.coroutines.flow.Flow

class FotoFunRepositoryImpl(
    private val dao: FotoFunDao
): FotoFunRepository {

    override suspend fun getSettingById(settingId: Int): Setting? {
        return dao.getSettingById(settingId)
    }

    override suspend fun getSettingValue(settingName: String): Long {
        return dao.getSettingValue(settingName)
    }

    override fun getSettings(): LiveData<List<Setting>> {
        return dao.getSettings()
    }

    override fun getSettingsFlow(): Flow<List<Setting>> {
        return dao.getSettingsFlow()
    }

    override suspend fun checkIfSettingsEmpty(): Boolean {
        return dao.checkIfSettingsEmpty()
    }

    override suspend fun addSetting(setting: Setting) {
        return dao.addSetting(setting)
    }

    override suspend fun updateSetting(settingName: String, settingValue: Long) {
        return dao.updateSetting(settingName, settingValue)
    }

    override suspend fun deleteTable() {
        dao.deleteTable()
    }
}