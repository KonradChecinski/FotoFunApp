package com.example.fotofun.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fotofun.data.entities.Email
import com.example.fotofun.data.entities.Setting


@Database(
    entities = [Setting::class, Email::class],
    version = 4
)
abstract class FotoFunDatabase: RoomDatabase() {

    abstract val dao: FotoFunDao
}