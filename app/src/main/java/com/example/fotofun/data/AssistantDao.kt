package com.example.fotofun.data

import androidx.room.*
import com.example.fotofun.data.entities.Course
import com.example.fotofun.data.entities.Grade
import com.example.fotofun.data.entities.Student
import com.example.fotofun.data.relations.CourseWithStudents
import com.example.fotofun.data.relations.CourseWithStudentsWithGrades
import com.example.fotofun.data.relations.StudentWithCourses
import com.example.fotofun.data.relations.StudentWithGrades
import kotlinx.coroutines.flow.Flow

@Dao
interface AssistantDao {

    //region Student
    @Query("SELECT * FROM Student")
    fun getStudents(): Flow<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    suspend fun getStudentById(studentId: Int): Student?

    @Transaction
    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    suspend fun getStudentWithGrades(studentId: Int): List<StudentWithGrades>
    //endregion


    //region Grade
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGrade(grade: Grade)

    @Query("SELECT * FROM Grade WHERE gradeId = :gradeId")
    suspend fun getGradeById(gradeId: Int): Grade?

    @Delete
    suspend fun deleteGrade(grade: Grade)

    //endregion


    //region Course
    @Query("SELECT * FROM Course")
    fun getCourses(): Flow<List<Course>>

    @Query("SELECT * FROM Course WHERE courseId = :courseId")
    suspend fun getCourseById(courseId: Int): Course?

    @Query("SELECT * FROM Course WHERE courseId = :courseId")
    fun getCourseByIdSync(courseId: Int): Flow<Course>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    //endregion

    //region CourseWithStudents

    @Query("SELECT * FROM Course WHERE courseId = :courseId")
    fun getStudentsInCourseById(courseId: Int): Flow<CourseWithStudents>

    @Query("DELETE FROM StudentCourseCrossRef WHERE courseId = :courseId AND studentId= :studentId")
    suspend fun deleteCourseWithStudents(courseId: Int, studentId: Int)

    //endregion

    //region StudentWithCourse

    @Query("SELECT * FROM Student")
    fun getStudentsWithCourse(): Flow<List<StudentWithCourses>>

    @Query("SELECT * FROM Student WHERE name LIKE '%' || :text || '%' OR lastName LIKE '%' || :text || '%' OR albumNumber LIKE '%' || :text || '%'")
    fun getStudentsWithCourseSearch(text: String): Flow<List<StudentWithCourses>>

    @Query("INSERT INTO StudentCourseCrossRef VALUES(:studentId, :courseId)")
    suspend fun addStudentWithCourse(courseId: Int, studentId: Int)

    @Query("DELETE FROM StudentCourseCrossRef  WHERE courseId = :courseId AND studentId= :studentId")
    suspend fun deleteStudentWithCourse(courseId: Int, studentId: Int)


    //endregion



    @Query("SELECT * FROM Course WHERE courseId = :courseId")
    fun getStudentsInCourseWithGradesById(courseId: Int): Flow<CourseWithStudentsWithGrades>

//    @Transaction
//    @Query("SELECT * FROM Student JOIN Grade ON (Student.studentId=Grade.studentId AND classId = :classId) WHERE studentId = :studentId")
//    suspend fun getStudentWithGradesInCourse(studentId: Int, classId: Int): List<StudentWithGrades>
}