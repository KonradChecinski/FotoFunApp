package com.example.fotofun.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.fotofun.data.entities.Grade
import com.example.fotofun.data.entities.Student

data class StudentWithGrades(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "studentId",
        entityColumn = "studentId"
    )
    val grades: List<Grade>
)
