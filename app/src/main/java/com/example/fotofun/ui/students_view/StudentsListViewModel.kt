package com.example.fotofun.ui.students_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.entities.Student
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.util.Routes
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentsListViewModel @Inject constructor(
    private val repository: AssistantRepository
): ViewModel() {

    val students = repository.getStudents()

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedStudent: Student? = null

    fun onEvent(event: StudentsListEvent) {
        when(event) {
            is StudentsListEvent.OnStudentClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.STUDENT_ADD_EDIT + "?studentId=${event.student.studentId}"))
            }
            is StudentsListEvent.OnAddStudentClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.STUDENT_ADD_EDIT))
            }
            is StudentsListEvent.OnUndoDeleteClick -> {
                deletedStudent?.let { student ->
                    viewModelScope.launch {
                        repository.addStudent(student)
                    }
                }
            }
            is StudentsListEvent.OnDeleteStudentClick -> {
                viewModelScope.launch {
                    deletedStudent = event.student
                    repository.deleteStudent(event.student)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Student usunięty",
                        action = "Cofnij"
                    ))
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