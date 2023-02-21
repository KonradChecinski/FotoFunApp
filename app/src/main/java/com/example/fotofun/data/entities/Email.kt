package com.example.fotofun.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Email(
    @PrimaryKey val emailId: Int? = null,
    val settingName: String,
    val settingValue: String
)
