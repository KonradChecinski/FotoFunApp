package com.example.fotofun.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fotofun.data.entities.Course
import com.example.fotofun.data.entities.Grade
import com.example.fotofun.data.entities.Student
import com.example.fotofun.data.relations.StudentCourseCrossRef

@Database(
    entities = [Student::class, Grade::class, Course::class, StudentCourseCrossRef::class],
    version = 6
)
abstract class AssistantDatabase: RoomDatabase() {

    abstract val dao: AssistantDao
}