package com.example.fotofun.ui.app_view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.system.Os.mkdir
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.fotofun.R
import com.example.fotofun.ui.camera_view.CameraView
import com.example.fotofun.ui.camera_view.CameraViewModel
import com.example.fotofun.util.UiEvent
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService



@Composable
fun AppView(
    onNavigate: (UiEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
    cameraExecutor: ExecutorService,
    shouldShowCamera: MutableState<Boolean>,
    outputDirectory: File
) {
    if(shouldShowCamera.value) {
        CameraView(
            outputDirectory = outputDirectory,
            executor = cameraExecutor,
            onImageCaptured = viewModel::handleImageCapture,
            onError = { Log.e("kilo", "View error:", it) }
        )
    }
    else {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = "Nie udzielono pozwolenia na wykorzystanie kamery",
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 40.sp
            )
        }
    }

    if (viewModel.shouldShowPhoto.value) {
        Image(
            painter = rememberImagePainter(viewModel.photoUri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        )
    }
}

@Composable
private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
    val parcelFileDescriptor = LocalContext.current.contentResolver.openFileDescriptor(selectedFileUri, "r")
    val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
    val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor.close()
    return image

    return null
}
