package com.example.fotofun.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fotofun.data.entities.Setting


@Database(
    entities = [Setting::class],
    version = 1
)
abstract class FotoFunDatabase: RoomDatabase() {

    abstract val dao: FotoFunDao
}