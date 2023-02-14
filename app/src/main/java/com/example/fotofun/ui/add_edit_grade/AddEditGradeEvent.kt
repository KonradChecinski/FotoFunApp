package com.example.fotofun.ui.add_edit_grade

import com.example.fotofun.data.relations.StudentWithCourses

sealed class AddEditGradeEvent {
    data class OnStudentClick(val studentWithCourses: StudentWithCourses): AddEditGradeEvent()
    data class OnCheckBoxClick(val isPoints: Boolean): AddEditGradeEvent()
    data class OnPointsChange(val text: String): AddEditGradeEvent()
    data class OnGradeChange(val text: String): AddEditGradeEvent()

    object OnSaveGradeClick: AddEditGradeEvent()
    object OnDeleteGradeClick: AddEditGradeEvent()
    object OnConfirmDeleteGradeClick: AddEditGradeEvent()
}
