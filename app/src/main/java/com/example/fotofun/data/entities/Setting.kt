package com.example.fotofun.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Setting(
    @PrimaryKey val settingId: Int? = null,
    val settingName: String,
    val settingValue: Long
)