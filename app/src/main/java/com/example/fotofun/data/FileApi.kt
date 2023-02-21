package com.example.fotofun.data

import androidx.annotation.StringRes
import com.example.fotofun.data.entities.SimpleJSONResponse
import okhttp3.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

interface FileApi {

    @POST("foto")
    suspend fun uploadImages(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ) : SimpleJSONResponse

    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("http://dom.webitup.pl/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FileApi::class.java)
        }
    }
}