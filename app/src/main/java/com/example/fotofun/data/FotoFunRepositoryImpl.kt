package com.example.fotofun.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.fotofun.data.entities.Email
import com.example.fotofun.data.entities.Setting
import com.example.fotofun.data.entities.SimpleJSONResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File


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

    override suspend fun addEmail(email: Email) {
        dao.addEmail(email)
    }

    override suspend fun getEmailById(emailId: Int): Email? {
        return dao.getEmailById(emailId)
    }

    override suspend fun uploadImages(images: List<File>, baner: Int, email: String): SimpleJSONResponse? {
        try{
            val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)

            images.forEach {
                multipart.addFormDataPart(
                    name = "images[]",
                    filename = it.name,
                    body = it.asRequestBody("image/*".toMediaType())
                )
            }
            multipart.addFormDataPart("baner", baner.toString())
            if(!email.isEmpty())  multipart.addFormDataPart("email", email)
            Log.i("gromzi", email)
            Log.i("gromzi", "Przed")

            val fetch = FileApi.instance.uploadImages("Bearer 1|tDaBvIUnT1Eq2E1au7KxGD8JfTqcTCQSnDU3ChR3", multipart.build())
            Log.i("gromzi", "po")

//            val result = fetch.execute()

            Log.i("gromzi", fetch.toString())
            return fetch

        }catch (e: Exception) {
            Log.i("gromzi", "exce")
            Log.i("gromzi", e.message.toString())

            e.printStackTrace()
        } catch (e: HttpException) {
            Log.i("gromzi", "exce2")
            e.printStackTrace()
        }
        return null
    }


    override suspend fun downloadPDF(fileUrl :String): Response<ResponseBody> {
        return FileApi.instance.downloadPDF(fileUrl)
    }
}