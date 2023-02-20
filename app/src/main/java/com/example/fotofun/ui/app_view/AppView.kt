package com.example.fotofun.ui.app_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint.Align
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.system.Os.mkdir
import android.util.Log
import android.util.Rational
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.fotofun.R
import com.example.fotofun.ui.camera_view.CameraView
import com.example.fotofun.ui.camera_view.CameraViewEvent
import com.example.fotofun.ui.camera_view.CameraViewModel
import com.example.fotofun.util.UiEvent
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Composable
fun AppView(
    onNavigate: (UiEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
    cameraExecutor: ExecutorService,
    shouldShowCamera: MutableState<Boolean>,
    outputDirectory: File
) {

    // SET DEFAULT SETTINGS
    viewModel.onEvent(AppViewEvent.OnAppLoad)
    val settings = viewModel.settings.collectAsState(initial = emptyList())



    // SETUP
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }
    val ratio = Rational(4,3)
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().setTargetAspectRatio(RATIO_4_3).build() }


    // 2
    LaunchedEffect(viewModel.lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            viewModel.cameraSelector,
            viewModel.preview,
            imageCapture
        )

        viewModel.preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    if(shouldShowCamera.value) {
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

            IconButton(
                modifier = Modifier.padding(bottom = 20.dp),
                onClick = {
                    Log.i("kilo", "ON CLICK")
                    viewModel.onEvent(
                        AppViewEvent.OnTakePhoto(
                            filenameFormat = "dd-MM-yyyy-HH-mm-ss-SSS",
                            imageCapture = imageCapture,
                            outputDirectory = outputDirectory,
                            executor = cameraExecutor,
                            onImageCaptured = viewModel::handleImageCapture,
                            onError = { Log.e("kilo", "View error:", it) },

                            howMany = if(!settings.value.isNullOrEmpty()) settings.value[0].settingValue.toInt() else 5,
                            delayMilliseconds = if(!settings.value.isNullOrEmpty()) settings.value[1].settingValue else 3000,
                            baner = if(!settings.value.isNullOrEmpty()) settings.value[2].settingValue.toInt() else 1,
                            email = ""
                        )
                    )
                },
                content = {
                    Icon(
                        imageVector = Icons.Sharp.Lens,
                        contentDescription = "Take picture",
                        tint = Color.White,
                        modifier = Modifier
                            .size(300.dp)
                            .padding(1.dp)
                            .border(1.dp, Color.White, CircleShape)
                    )
                }
            )
        }
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

    if (viewModel.shouldShowPopup.value) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 270.dp)
                .size(300.dp, 150.dp)
                .background(color = MaterialTheme.colors.background)
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                    ,
                    text = "Jak przesłać zdjęcia?"
                )

                Row() {
                    Button(
                        modifier = Modifier.size(150.dp, 80.dp).padding(5.dp, 10.dp),
                        onClick = {
                            viewModel.onPopupPressPdf(
                                baner = if(!settings.value.isNullOrEmpty()) settings.value[2].settingValue.toInt() else 1
                            )
                        }
                    ) {
                        Text(text = "Pobierz pdf")
                    }
                    Button(
                        modifier = Modifier.size(150.dp, 80.dp).padding(5.dp, 10.dp),
                        onClick = {
                            viewModel.onPopupPressEmail(
                                baner = if(!settings.value.isNullOrEmpty()) settings.value[2].settingValue.toInt() else 1,
                                email = viewModel.email.toString()
                            )
                        }
                    ) {
                        Text(text = "Wyślij na maila")
                    }
                }
            }
        }
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

