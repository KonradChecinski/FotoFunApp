package com.example.fotofun.ui.add_edit_course

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.data.entities.Course
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCourseViewModel @Inject constructor(
    private val repository: AssistantRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var course by mutableStateOf<Course?>(null)
        private set

    var courseName by mutableStateOf("")
        private set

    var weekDay by mutableStateOf("")
        private set

    var timeBlockFrom by mutableStateOf("")
        private set

    var timeBlockTo by mutableStateOf("")
        private set

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val courseId = savedStateHandle.get<Int>("courseId")!!
        if(courseId != -1) {
            viewModelScope.launch {
                repository.getCourseById(courseId)?.let { course ->
                    courseName = course.courseName
                    weekDay = course.weekDay.toString()
                    timeBlockFrom = course.timeBlockFrom
                    timeBlockTo = course.timeBlockTo
                    this@AddEditCourseViewModel.course = course
                }
            }
        }
    }

    fun onEvent(event: AddEditCourseEvent) {
        when(event) {
            is AddEditCourseEvent.OnCourseNameChange -> {
                courseName = event.courseName
            }
            is AddEditCourseEvent.OnWeekDayChange -> {
                weekDay = event.weekDay
            }
            is AddEditCourseEvent.OnTimeFromBlockChange -> {
                timeBlockFrom = event.timeBlockFrom
            }
            is AddEditCourseEvent.OnTimeToBlockChange -> {
                timeBlockTo = event.timeBlockTo
            }
            is AddEditCourseEvent.OnSaveCourseClick -> {
                viewModelScope.launch {
                    if (courseName.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Nazwa przedmiotu nie może być pusta"
                            )
                        )
                        return@launch
                    }
                    if (weekDay.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Dzień tygodnia nie może być pusty"
                            )
                        )
                        return@launch
                    }
                    if (timeBlockFrom.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Przedział czasowy nie może być pusty"
                            )
                        )
                        return@launch
                    }
                    if (timeBlockTo.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Przedział czasowy nie może być pusty"
                            )
                        )
                        return@launch
                    }
                    repository.addCourse(
                        Course(
                            courseName = courseName,
                            weekDay = weekDay,
                            timeBlockFrom = timeBlockFrom,
                            timeBlockTo = timeBlockTo,
                            courseId = course?.courseId
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