package com.example.fotofun.ui.add_edit_student

sealed class AddEditStudentEvent {
    data class OnNameChange(val name: String): AddEditStudentEvent()
    data class OnLastnameChange(val lastname: String): AddEditStudentEvent()
    data class OnAlbumNumberChange(val albumNumber: String): AddEditStudentEvent()
    object OnSaveStudentClick: AddEditStudentEvent()
}
