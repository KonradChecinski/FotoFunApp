package com.example.fotofun.ui.add_edit_student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.entities.Student
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditStudentViewModel @Inject constructor(
    private val repository: AssistantRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var student by mutableStateOf<Student?>(null)
        private set

    var name by mutableStateOf("")
        private set

    var lastname by mutableStateOf("")
        private set

    var albumNumber by mutableStateOf("")
        private set

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val studentId = savedStateHandle.get<Int>("studentId")!!
        if(studentId != -1) {
            viewModelScope.launch {
                repository.getStudentById(studentId)?.let { student ->
                    name = student.name
                    lastname = student.lastName
                    albumNumber = student.albumNumber
                    this@AddEditStudentViewModel.student = student
                }
            }
        }
    }

    fun onEvent(event: AddEditStudentEvent) {
        when(event) {
            is AddEditStudentEvent.OnNameChange -> {
                name = event.name
            }
            is AddEditStudentEvent.OnLastnameChange -> {
                lastname = event.lastname
            }
            is AddEditStudentEvent.OnAlbumNumberChange -> {
                albumNumber = event.albumNumber
            }
            is AddEditStudentEvent.OnSaveStudentClick -> {
                viewModelScope.launch {
                    if (name.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Imię nie może być puste"
                            )
                        )
                        return@launch
                    }
                    if (lastname.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Nazwisko nie może być puste"
                            )
                        )
                        return@launch
                    }
                    if (albumNumber.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Numer albumu nie może być puste"
                            )
                        )
                        return@launch
                    }
                    repository.addStudent(
                        Student(
                            name = name,
                            lastName = lastname,
                            albumNumber = albumNumber,
                            studentId = student?.studentId
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}