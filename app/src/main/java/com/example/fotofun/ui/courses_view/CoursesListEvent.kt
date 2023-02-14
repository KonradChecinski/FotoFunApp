package com.example.fotofun.ui.courses_view

import com.example.fotofun.data.entities.Course

sealed class CoursesListEvent {
    data class OnDeleteCourseClick(val course: Course): CoursesListEvent()
    object OnUndoDeleteClick: CoursesListEvent()
    data class OnCourseClick(val course: Course): CoursesListEvent()
    data class OnEditCourseClick(val course: Course): CoursesListEvent()
    object OnAddCourseClick: CoursesListEvent()
}
