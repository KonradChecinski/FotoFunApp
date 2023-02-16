package com.example.fotofun.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Setting(
    @PrimaryKey val optionId: Int? = null,
    val settingName: String,
    val settingValue: Long
)