package com.example.fotofun.ui.add_edit_grade

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.data.entities.Grade
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditGradeViewModel @Inject constructor(
    private val repository: AssistantRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val tmpInt: Int? = null
    val tmpDbl: Double? = null

    var grade by mutableStateOf<Grade?>(null)
        private set

    var isPoints by mutableStateOf(false)
        private set

    var points by mutableStateOf(tmpInt)
        private set

    var gradeValue by mutableStateOf(tmpDbl)
        private set

    var courseId by mutableStateOf(0)
        private set

    var studentId by mutableStateOf(0)
        private set


    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init {
        val gradeId = savedStateHandle.get<Int>("gradeId")!!
        courseId = savedStateHandle.get<Int>("courseId")!!
        studentId = savedStateHandle.get<Int>("studentId")!!
        if(gradeId != -1) {
            viewModelScope.launch {
                repository.getGradeById(gradeId)?.let { grade ->
                    isPoints = grade.isPoints
                    points = grade.points
                    gradeValue = grade.gradeValue
                    courseId = grade.classId
                    studentId = grade.studentId

                    this@AddEditGradeViewModel.grade = grade
                }
            }
        }
    }


    fun onEvent(event: AddEditGradeEvent) {
        when(event) {
            is AddEditGradeEvent.OnSaveGradeClick -> {
                viewModelScope.launch {
                    if (isPoints) {
                        if (points.toString().isBlank()) {
                            sendUiEvent(
                                UiEvent.ShowSnackbar(
                                    message = "Punkty nie mogą być puste"
                                )
                            )
                            return@launch
                        }
                    }else{
                        if (gradeValue.toString().isBlank()) {
                            sendUiEvent(
                                UiEvent.ShowSnackbar(
                                    message = "Ocena nie może być pusta"
                                )
                            )
                            return@launch
                        }
                    }

                    repository.addGrade(
                        Grade(
                            gradeId = grade?.gradeId,
                            studentId = studentId,
                            classId = courseId,
                            isPoints = isPoints,
                            gradeValue = gradeValue,
                            points = points,
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }

            is AddEditGradeEvent.OnDeleteGradeClick -> {
                if(grade != null) {
                    viewModelScope.launch {
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = "Czy na pewno chcesz usunąc ocenę?",
                            action = "Tak"
                        ))
                    }
                }

            }

            is AddEditGradeEvent.OnConfirmDeleteGradeClick -> {
                viewModelScope.launch {
                    repository.deleteGrade(grade!!)
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }

            is AddEditGradeEvent.OnCheckBoxClick -> {
                isPoints = event.isPoints
            }
            is AddEditGradeEvent.OnPointsChange -> {
                points = event.text.toIntOrNull()
            }
            is AddEditGradeEvent.OnGradeChange -> {
                gradeValue = event.text.toDoubleOrNull()
            }

        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}