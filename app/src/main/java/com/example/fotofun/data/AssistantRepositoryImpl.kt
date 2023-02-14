package com.example.fotofun.data

import com.example.fotofun.data.entities.Course
import com.example.fotofun.data.entities.Grade
import com.example.fotofun.data.entities.Student
import com.example.fotofun.data.relations.CourseWithStudents
import com.example.fotofun.data.relations.CourseWithStudentsWithGrades
import com.example.fotofun.data.relations.StudentWithCourses
import com.example.fotofun.data.relations.StudentWithGrades
import kotlinx.coroutines.flow.Flow

class AssistantRepositoryImpl(
    private val dao: AssistantDao
): AssistantRepository {
    //Student
    override fun getStudents(): Flow<List<Student>> {
        return dao.getStudents()
    }
    override suspend fun addStudent(student: Student) {
        dao.addStudent(student)
    }

    override suspend fun deleteStudent(student: Student) {
        dao.deleteStudent(student)
    }

    override suspend fun getStudentById(studentId: Int): Student? {
        return dao.getStudentById(studentId)
    }

    override suspend fun getStudentWithGrades(studentId: Int): List<StudentWithGrades> {
        return dao.getStudentWithGrades(studentId)
    }


    //Grade
    override suspend fun addGrade(grade: Grade) {
        dao.addGrade(grade)
    }

    override suspend fun getGradeById(gradeId: Int): Grade? {
        return dao.getGradeById(gradeId)
    }

    override suspend fun deleteGrade(grade: Grade) {
        dao.deleteGrade(grade)
    }

    //Course

    override fun getCourses(): Flow<List<Course>> {
        return dao.getCourses()
    }

    override suspend fun getCourseById(courseId: Int): Course? {
        return dao.getCourseById(courseId)
    }

    override fun getCourseByIdSync(courseId: Int): Flow<Course> {
        return dao.getCourseByIdSync(courseId)
    }

    override fun getStudentsInCourseById(courseId: Int): Flow<CourseWithStudents> {
        return dao.getStudentsInCourseById(courseId)
    }

    override suspend fun addCourse(course: Course) {
        return dao.addCourse(course)
    }

    override suspend fun deleteCourse(course: Course) {
        dao.deleteCourse(course)
    }

    override suspend fun deleteCourseWithStudents(courseId: Int, studentId: Int){
        dao.deleteCourseWithStudents(courseId, studentId)
    }

    override fun getStudentsWithCourse(): Flow<List<StudentWithCourses>> {
        return dao.getStudentsWithCourse()
    }

    override fun getStudentsWithCourseSearch(text: String): Flow<List<StudentWithCourses>> {
        return dao.getStudentsWithCourseSearch(text)
    }


    override suspend fun addStudentWithCourse(courseId: Int, studentId: Int){
        dao.addStudentWithCourse(courseId, studentId)
    }

    override suspend fun deleteStudentWithCourse(courseId: Int, studentId: Int){
        dao.deleteStudentWithCourse(courseId, studentId)
    }



    override fun getStudentsInCourseWithGradesById(courseId: Int): Flow<CourseWithStudentsWithGrades> {
        return dao.getStudentsInCourseWithGradesById(courseId)
    }
//    override suspend fun getStudentWithGradesInCourse(studentId: Int, classId: Int): List<StudentWithGrades>{
//        return dao.getStudentWithGradesInCourse(studentId, classId)
//    }

}