package com.example.fotofun.data

import com.example.fotofun.data.entities.Course
import com.example.fotofun.data.entities.Grade
import com.example.fotofun.data.entities.Student
import com.example.fotofun.data.relations.CourseWithStudents
import com.example.fotofun.data.relations.CourseWithStudentsWithGrades
import com.example.fotofun.data.relations.StudentWithCourses
import com.example.fotofun.data.relations.StudentWithGrades
import kotlinx.coroutines.flow.Flow

interface AssistantRepository {
    //Student
    fun getStudents(): Flow<List<Student>>

    suspend fun addStudent(student: Student)

    suspend fun deleteStudent(student: Student)

    suspend fun getStudentById(studentId: Int): Student?

    suspend fun getStudentWithGrades(studentId: Int): List<StudentWithGrades>

    //Grade
    suspend fun addGrade(grade: Grade)

    suspend fun getGradeById(gradeId: Int): Grade?

    suspend fun deleteGrade(grade: Grade)


    //Course

    fun getCourses(): Flow<List<Course>>

    suspend fun getCourseById(courseId: Int): Course?
    fun getCourseByIdSync(courseId: Int): Flow<Course>

    fun getStudentsInCourseById(courseId: Int): Flow<CourseWithStudents>

    suspend fun addCourse(course: Course)

    suspend fun deleteCourse(course: Course)

    suspend fun deleteCourseWithStudents(courseId: Int, studentId: Int)

    fun getStudentsWithCourse(): Flow<List<StudentWithCourses>>

    fun getStudentsWithCourseSearch(text: String): Flow<List<StudentWithCourses>>


    suspend fun addStudentWithCourse(courseId: Int, studentId: Int)

    suspend fun deleteStudentWithCourse(courseId: Int, studentId: Int)



    fun getStudentsInCourseWithGradesById(courseId: Int): Flow<CourseWithStudentsWithGrades>
//    suspend fun getStudentWithGradesInCourse(studentId: Int, classId: Int): List<StudentWithGrades>

}