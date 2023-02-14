package com.example.fotofun.ui.add_edit_course_with_student_view

import com.example.fotofun.data.relations.StudentWithCourses

sealed class AddEditCourseWithStudentEvent {
    data class OnStudentClick(val studentWithCourses: StudentWithCourses): AddEditCourseWithStudentEvent()
    data class OnSearchBarChange(val text: String): AddEditCourseWithStudentEvent()
}
