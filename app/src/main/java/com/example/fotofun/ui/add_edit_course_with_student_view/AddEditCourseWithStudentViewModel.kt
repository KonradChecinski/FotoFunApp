package com.example.fotofun.ui.add_edit_course_with_student_view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCourseWithStudentViewModel @Inject constructor(
    private val repository: AssistantRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var courseId: Int? = savedStateHandle.get("courseId")

    var text by mutableStateOf("")
        private set



    var studentsWithCourse = repository.getStudentsWithCourseSearch(text)
    val course = repository.getCourseByIdSync(courseId!!)

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()




    fun onEvent(event: AddEditCourseWithStudentEvent) {
        when(event) {
            is AddEditCourseWithStudentEvent.OnStudentClick -> {
                viewModelScope.launch {
                    if (event.studentWithCourses.courses.any{ course -> course.courseId == courseId}){
                        repository.deleteStudentWithCourse(courseId!!, event.studentWithCourses.student.studentId!!)
                    }else{
                        repository.addStudentWithCourse(courseId!!, event.studentWithCourses.student.studentId!!)
                    }
                }
            }
            is AddEditCourseWithStudentEvent.OnSearchBarChange -> {
                text = event.text
                studentsWithCourse = repository.getStudentsWithCourseSearch(text)
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}