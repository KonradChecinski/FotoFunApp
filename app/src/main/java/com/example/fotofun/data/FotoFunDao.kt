package com.example.fotofun.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FotoFunDao {

    @Query("SELECT settingValue FROM Setting WHERE settingName = :settingName")
    fun getSettingValue(settingName: String): Long
}