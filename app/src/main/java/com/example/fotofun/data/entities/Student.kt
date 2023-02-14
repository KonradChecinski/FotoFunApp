package com.example.fotofun.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey val studentId: Int? = null,
    val name: String,
    val lastName: String,
    val albumNumber: String,
)
