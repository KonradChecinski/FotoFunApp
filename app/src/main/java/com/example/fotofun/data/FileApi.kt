package com.example.fotofun.data

import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.Part

interface FileApi {

    @Multipart
    suspend fun uploadImages(
        @Part images: MultipartBody.Part
    )

    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("http://dom.webitup.pl/api/foto/")
                .build()
                .create(FileApi::class.java)
        }
    }
}