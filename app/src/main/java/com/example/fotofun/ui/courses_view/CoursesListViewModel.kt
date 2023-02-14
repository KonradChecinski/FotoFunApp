package com.example.fotofun.ui.courses_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.data.entities.Course
import com.example.fotofun.util.Routes
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesListViewModel @Inject constructor(
    private val repository: AssistantRepository
): ViewModel() {

    val courses = repository.getCourses()

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedCourse: Course? = null

    fun onEvent(event: CoursesListEvent) {
        when(event) {
            is CoursesListEvent.OnCourseClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.COURSE_STUDENT_LIST + "/${event.course.courseId}"))
            }
            is CoursesListEvent.OnEditCourseClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.COURSE_ADD_EDIT + "?courseId=${event.course.courseId}"))
            }
            is CoursesListEvent.OnAddCourseClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.COURSE_ADD_EDIT))
            }
            is CoursesListEvent.OnUndoDeleteClick -> {
                deletedCourse?.let { course ->
                    viewModelScope.launch {
                        repository.addCourse(course)
                    }
                }
            }
            is CoursesListEvent.OnDeleteCourseClick -> {
                viewModelScope.launch {
                    deletedCourse = event.course
                    repository.deleteCourse(event.course)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Przedmiot usunięty",
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