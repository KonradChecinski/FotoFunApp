package com.example.fotofun.ui.add_edit_course

sealed class AddEditCourseEvent {
    data class OnCourseNameChange(val courseName: String): AddEditCourseEvent()
    data class OnWeekDayChange(val weekDay: String): AddEditCourseEvent()
    data class OnTimeFromBlockChange(val timeBlockFrom: String): AddEditCourseEvent()
    data class OnTimeToBlockChange(val timeBlockTo: String): AddEditCourseEvent()
    object OnSaveCourseClick: AddEditCourseEvent()
}
