package com.example.fotofun.ui.course_with_student_view

import androidx.lifecycle.SavedStateHandle
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
class CourseWithStudentListViewModel @Inject constructor(
    private val repository: AssistantRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var courseId: Int? = savedStateHandle.get("courseId")

//    val students = repository.get()
//    val course = repository.getStudentsInCourseById(courseId!!)
    val course = repository.getStudentsInCourseWithGradesById(courseId!!)

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedStudent: Student? = null

    fun onEvent(event: CourseWithStudentListEvent) {
        when(event) {
            is CourseWithStudentListEvent.OnStudentClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.STUDENT_ADD_EDIT + "?studentId=${event.student.studentId}"))
            }
            is CourseWithStudentListEvent.OnAddStudentClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.STUDENT_ADD_EDIT))
            }
            is CourseWithStudentListEvent.OnUndoDeleteClick -> {
                deletedStudent?.let { student ->
                    viewModelScope.launch {
                        repository.addStudent(student)
                    }
                }
            }
            is CourseWithStudentListEvent.OnDeleteStudentClick -> {
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