package com.example.fotofun.di

import com.example.fotofun.data.entities.SimpleJSONResponse
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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