package com.example.fotofun.ui.app_view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileDescriptor
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: AssistantRepository,
): ViewModel() {

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    lateinit var photoUri: Uri
    var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)

    var photos: MutableList<Bitmap?> = mutableListOf<Bitmap?>()


    // SETUP
    val lensFacing = CameraSelector.LENS_FACING_FRONT
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    val preview = Preview.Builder().build()

    fun onEvent(event: AppViewEvent) {
        when(event) {
            is AppViewEvent.OnTakePhoto -> {
                takePhotos(
                    event.filenameFormat,
                    event.imageCapture,
                    event.outputDirectory,
                    event.executor,
                    event.onImageCaptured,
                    event.onError,

                    event.howMany,
                    event.delayMilliseconds
                )
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun handleImageCapture(uri: Uri) {
        Log.i("kilo", "Image captured: $uri")
//        shouldShowCamera.value = false

        photoUri = uri
        shouldShowPhoto.value = true


    }
    fun asasa(){

        Handler(Looper.getMainLooper()).postDelayed(
            {
                shouldShowPhoto.value = false
    //            photos.add(uriToBitmap(photoUri))


                takePhotoSeries(5, 1000)
            },
            3000
        )
    }

    private fun takePhotoSeries(index: Int, delayMilliseconds: Long) {
        viewModelScope.launch {
            for (i in 0..index) {
                Log.i("tag", "This'll run $delayMilliseconds ms later: $i")
                delay(delayMilliseconds)
            }
        }
    }

    private fun takePhotos(
        filenameFormat: String,
        imageCapture: ImageCapture,
        outputDirectory: File,
        executor: Executor,
        onImageCaptured: (Uri) -> Unit,
        onError: (ImageCaptureException) -> Unit,

        howMany: Int,
        delayMilliseconds: Long
    ) {

        viewModelScope.launch {

            for (i in 0..howMany) {

                if(i <= howMany - 1) {
                    Log.i("tag", "This'll run $delayMilliseconds ms later: $i")

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
                else {
                    shouldShowPhoto.value = false
                }

                shouldShowPhoto.value = false
                delay(delayMilliseconds)
            }
        }
    }

    fun uriToBitmapAppViewModel(parcelFileDescriptor: ParcelFileDescriptor): Bitmap? {

        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image

        return null
    }
}