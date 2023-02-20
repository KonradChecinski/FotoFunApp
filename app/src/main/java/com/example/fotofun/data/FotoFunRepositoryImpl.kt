package com.example.fotofun.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.fotofun.data.entities.Setting
import com.example.fotofun.data.entities.SimpleJSONResponse
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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

    @SuppressLint("SuspiciousIndentation")
    override suspend fun uploadImages(images: List<File>, baner: Int, email: String) {
        try{
            val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)

            images.forEach {
                multipart.addFormDataPart(
                    name = "images[]",
                    filename = images[0].name,
                    body = it.asRequestBody("image/*".toMediaType())
                )
            }
            multipart.addFormDataPart("baner", baner.toString())
            if(!email.isEmpty())  multipart.addFormDataPart("email", email)
                        Log.i("gromzi", "Przed")

            val fetch = FileApi.instance.uploadImages("Bearer 1|tDaBvIUnT1Eq2E1au7KxGD8JfTqcTCQSnDU3ChR3", multipart.build())
            Log.i("gromzi", "po")

            val result = fetch.execute().raw()

            Log.i("gromzi", result.toString())


        }catch (e: Exception) {
            Log.i("gromzi", "exce")

            e.printStackTrace()
        } catch (e: HttpException) {
            Log.i("gromzi", "exce2")
            e.printStackTrace()
        }

//
//                if (response.isSuccessful) {
//
//                    val items = response.body()
//                    if (items != null) {
//                        Log.i("gromzi", items.result.toString())
//                    }
////                        for (i in 0 until items.count()) {
////                            // ID
////                            val id = items[i].employeeId ?: "N/A"
////                            Log.d("ID: ", id)
////
////                            // Employee Name
////                            val employeeName = items[i].employeeName ?: "N/A"
////                            Log.d("Employee Name: ", employeeName)
////
////                            // Employee Salary
////                            val employeeSalary = items[i].employeeSalary ?: "N/A"
////                            Log.d("Employee Salary: ", employeeSalary)
////
////                            // Employee Age
////                            val employeeAge = items[i].employeeAge ?: "N/A"
////                            Log.d("Employee Age: ", employeeAge)
////
////                        }
//
//                } else {
//
//                    Log.e("RETROFIT_ERROR", response.code().toString())
//                    Log.e("RETROFIT_ERROR", response.body()?.message.toString())
//                    Log.e("RETROFIT_ERROR", response.body()?.status.toString())
//                    Log.e("RETROFIT_ERROR", response.body()?.result.toString())
//                }





//        try {
//
//
//                .enqueue(object: Callback<SimpleJSONResponse>{
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    Log.d("gromzi", response.body()?.string().toString())
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
////                    Log.d("gromzi", t.toString())
//
//                }
//
//            })
//            true
//        } catch (e: IOException) {
//            e.printStackTrace()
//            false
//        } catch (e: HttpException) {
//            e.printStackTrace()
//            false
//        }
    }
}