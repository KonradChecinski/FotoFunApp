package com.example.fotofun.data

interface FotoFunRepository {

    fun getSettingValue(settingName: String): Long
}