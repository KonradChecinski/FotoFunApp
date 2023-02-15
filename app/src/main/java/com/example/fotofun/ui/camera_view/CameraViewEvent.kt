package com.example.fotofun.ui.camera_view

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import java.io.File
import java.util.concurrent.Executor

sealed class CameraViewEvent {
    data class OnTakePhoto(
        val filenameFormat: String,
        val imageCapture: ImageCapture,
        val outputDirectory: File,
        val executor: Executor,
        val onImageCaptured: (Uri) -> Unit,
        val onError: (ImageCaptureException) -> Unit

    ): CameraViewEvent()
}