package com.example.fotofun.data

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import com.example.fotofun.data.entities.Setting
import com.example.fotofun.data.entities.SimpleJSONResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException

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
    suspend fun uploadImages(images: List<File>, baner: Int, email: String)

}