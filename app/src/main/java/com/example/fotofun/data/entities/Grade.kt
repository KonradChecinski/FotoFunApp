package com.example.fotofun.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Grade(
    @PrimaryKey val gradeId: Int? = null,
    val studentId: Int,
    val classId: Int,

    val isPoints: Boolean,

    val gradeValue: Double?,
    val points: Int?,
) {
    companion object {
        val gradeValues = listOf(2, 2.5, 3, 3.5, 4, 4.5, 5)
    }
}