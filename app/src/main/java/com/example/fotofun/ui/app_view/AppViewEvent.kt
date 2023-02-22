package com.example.fotofun.ui.app_view

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
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
        val delayMilliseconds: Long,
        val baner: Int,
        val email: String

    ): AppViewEvent()

    object OnAppLoad: AppViewEvent()
    data class OnSetPhotosQuantity(val settingValue: Long): AppViewEvent()
    data class OnSetDelay(val settingValue: Long): AppViewEvent()
    data class OnSetBanner(val settingValue: Long): AppViewEvent()
    data class OnUpdateEmail(val settingValue: String): AppViewEvent()

    data class OnGetSettingValue(val settingName: String): AppViewEvent()
}
