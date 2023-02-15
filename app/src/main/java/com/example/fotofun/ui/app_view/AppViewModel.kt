package com.example.fotofun.ui.app_view

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberImagePainter
import com.example.fotofun.FotoFun
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.ui.camera_view.CameraViewEvent
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.FileDescriptor
import java.io.IOException
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

    fun onEvent(event: CameraViewEvent) {
        when(event) {

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


            doSomething(5)
        },
        3000
    )
}

    private fun doSomething(index: Int) {
        viewModelScope.launch {
            for (i in 0..index) {
                Log.i("tag", "This'll run 1 seconds later: $i")
                delay(1000)
            }
        }
    }



}