package com.example.fotofun.ui.camera_view

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.data.entities.Course
import com.example.fotofun.ui.courses_view.CoursesListEvent
import com.example.fotofun.util.Routes
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: AssistantRepository
): ViewModel() {

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun takePhoto(
        filenameFormat: String,
        imageCapture: ImageCapture,
        outputDirectory: File,
        executor: Executor,
        onImageCaptured: (Uri) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                Log.e("kilo", "Take photo error:", exception)
                onError(exception)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                onImageCaptured(savedUri)
            }
        })
    }

//    private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
//        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
//            cameraProvider.addListener({
//                continuation.resume(cameraProvider.get())
//            }, ContextCompat.getMainExecutor(this))
//        }
//    }

    fun onEvent(event: CameraViewEvent) {
        when(event) {
//            is CameraViewEvent.OnTakePhoto -> {
//                takePhoto(event.)
//            }
//            is CoursesListEvent.OnEditCourseClick -> {
//                sendUiEvent(UiEvent.Navigate(Routes.COURSE_ADD_EDIT + "?courseId=${event.course.courseId}"))
//            }
//            is CoursesListEvent.OnAddCourseClick -> {
//                sendUiEvent(UiEvent.Navigate(Routes.COURSE_ADD_EDIT))
//            }
//            is CoursesListEvent.OnUndoDeleteClick -> {
//                deletedCourse?.let { course ->
//                    viewModelScope.launch {
//                        repository.addCourse(course)
//                    }
//                }
//            }
//            is CoursesListEvent.OnDeleteCourseClick -> {
//                viewModelScope.launch {
//                    deletedCourse = event.course
//                    repository.deleteCourse(event.course)
//                    sendUiEvent(
//                        UiEvent.ShowSnackbar(
//                        message = "Przedmiot usunięty",
//                        action = "Cofnij"
//                    ))
//                }
//            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}