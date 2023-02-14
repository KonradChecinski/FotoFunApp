package com.example.fotofun.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.fotofun.data.entities.Course
import com.example.fotofun.data.entities.Student

data class CourseWithStudentsWithGrades(
    @Embedded val course: Course,
    @Relation(
        entity = Student::class,
        parentColumn = "courseId",
        entityColumn = "studentId",
        associateBy = Junction(StudentCourseCrossRef::class)
    )
    val students: List<StudentWithGrades>
)
