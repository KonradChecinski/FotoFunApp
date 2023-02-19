package com.example.fotofun.data

import androidx.lifecycle.LiveData
import com.example.fotofun.data.entities.Setting
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
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

    suspend fun uploadImages(images: List<File>): Boolean {

        return try {
            FileApi.instance.uploadImages(
                images = MultipartBody.Part
                    .createFormData(
                        "images",
                        images[0].name,
                        images[0].asRequestBody()
                    )
            )
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: HttpException) {
            e.printStackTrace()
            false
        }
    }
}