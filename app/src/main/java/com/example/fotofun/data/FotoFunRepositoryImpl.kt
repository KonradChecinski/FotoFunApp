package com.example.fotofun.data

class FotoFunRepositoryImpl(
    private val dao: FotoFunDao
): FotoFunRepository {

    override fun getSettingValue(settingName: String): Long {
        return dao.getSettingValue(settingName)
    }
}