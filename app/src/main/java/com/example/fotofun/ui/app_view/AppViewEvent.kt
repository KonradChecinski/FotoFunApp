package com.example.fotofun.ui.app_view

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.example.fotofun.ui.camera_view.CameraViewEvent
import java.io.File
import java.util.concurrent.Executor

sealed class AppViewEvent {
    data class OnTakePhoto(
        val filenameFormat: String,
        val imageCapture: ImageCapture,
        val outputDirectory: File,
        val executor: Executor,
        val onImageCaptured: (Uri) -> Unit,
        val onError: (ImageCaptureException) -> Unit,

        val howMany: Int,
        val delayMilliseconds: Long

    ): AppViewEvent()
}
