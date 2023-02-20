package com.example.fotofun.data.entities

import com.google.gson.annotations.SerializedName

data class SimpleJSONResponse(

@SerializedName("status")
var status: Boolean?,

@SerializedName("result")
var result: String?,

@SerializedName("message")
var message: String?,


)
