package com.example.fotofun.data

import androidx.lifecycle.LiveData
import com.example.fotofun.data.entities.Email
import com.example.fotofun.data.entities.Setting
import com.example.fotofun.data.entities.SimpleJSONResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FotoFunRepository {
    suspend fun getSettingById(settingId: Int): Setting?

    suspend fun getSettingValue(settingName: String): Long

    fun getSettings(): LiveData<List<Setting>>

    fun getSettingsFlow(): Flow<List<Setting>>

    suspend fun checkIfSettingsEmpty(): Boolean

    suspend fun addSetting(setting: Setting)

    suspend fun updateSetting(settingName: String, settingValue: Long)

    suspend fun deleteTable()
//    Response<StringRes>


    suspend fun addEmail(email: Email)

    suspend fun getEmailById(emailId: Int): Email?

    suspend fun uploadImages(images: List<File>, baner: Int, email: String) :SimpleJSONResponse?

}