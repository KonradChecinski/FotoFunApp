package com.example.fotofun.data.entities

import com.google.gson.annotations.SerializedName

data class SimpleJSONResponse(

@SerializedName("status")
var status: Boolean?,

@SerializedName("result")
var result: FotoPaper?,

@SerializedName("message")
var message: String?,


)


data class FotoPaper(
    @SerializedName("id")
    var id: String?,

    @SerializedName("pdf")
    var pdf: String?,

    @SerializedName("images")
    var images: List<String>,



    @SerializedName("updated_at")
    var updated_at: String?,
    @SerializedName("created_at")
    var created_at: String?,
)