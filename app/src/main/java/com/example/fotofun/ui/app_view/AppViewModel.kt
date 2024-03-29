package com.example.fotofun.ui.app_view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaActionSound
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.FotoFun
import com.example.fotofun.data.FotoFunRepository
import com.example.fotofun.data.entities.Email
import com.example.fotofun.data.entities.Setting
import com.example.fotofun.di.AndroidDownloader
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject


@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: FotoFunRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val applicationContext = FotoFun.applicationContext()

    var email: MutableState<String> = mutableStateOf("")

    val settings = repository.getSettingsFlow()
    val settingsLiveData = repository.getSettings()
    var checkIfSettingsEmpty: Boolean = false

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    lateinit var photoUri: Uri
    var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)
    var shouldShowPopup: MutableState<Boolean> = mutableStateOf(false)

    var images: MutableList<File> = mutableListOf<File>()


    // SETUP
    val lensFacing = CameraSelector.LENS_FACING_FRONT
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    var preview = Preview.Builder().setTargetAspectRatio(RATIO_4_3).build()


    init {
            viewModelScope.launch {
                repository.getEmailById(1)?.let { emailSet ->
                    email.value = emailSet.settingValue
                }
            }

    }


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
                    event.delayMilliseconds,
                    event.baner,
                    event.email
                )
            }

            is AppViewEvent.OnAppLoad -> {
                Log.i("gromzi", "getSettings(): ${repository.getSettings().toString()}" )
                viewModelScope.launch{
                    checkIfSettingsEmpty = repository.checkIfSettingsEmpty()
//                                        repository.deleteTable()

                    if (checkIfSettingsEmpty) {
                        Log.i("gromzi", "getSettings() było null")
                        viewModelScope.launch {
                            repository.addSetting(
                                setting = Setting(
                                    settingId = 1,
                                    settingName = "photosQuantity",
                                    settingValue = 5
                                )
                            )
                            repository.addSetting(
                                setting = Setting(
                                    settingId = 2,
                                    settingName = "photosDelay",
                                    settingValue = 3000
                                )
                            )
                            repository.addSetting(
                                setting = Setting(
                                    settingId = 3,
                                    settingName = "banner",
                                    settingValue = 1
                                )
                            )
                            repository.addEmail(
                                email = Email(
                                    emailId = 1,
                                    settingName = "email",
                                    settingValue = ""
                                )
                            )
//                        repository.deleteTable()
                        }
                    }

                }
            }

            is AppViewEvent.OnSetPhotosQuantity -> {
                Log.d("gromzi", "quantity")
                viewModelScope.launch {
                    repository.addSetting(
                        setting = Setting(
                            settingId = 1,
                            settingName = "photosQuantity",
                            settingValue = event.settingValue
                        )
                    )
                }
            }

            is AppViewEvent.OnSetDelay -> {
                viewModelScope.launch {
                    repository.addSetting(
                        setting = Setting(
                            settingId = 2,
                            settingName = "photosDelay",
                            settingValue = event.settingValue
                        )
                    )

                }
            }

            is AppViewEvent.OnSetBanner -> {
                viewModelScope.launch {
                    repository.addSetting(
                        setting = Setting(
                            settingId = 3,
                            settingName = "banner",
                            settingValue = event.settingValue
                        )
                    )
                }
            }

            is AppViewEvent.OnUpdateEmail -> {
                    email.value = event.settingValue
                viewModelScope.launch {
                    repository.addEmail(
                        email = Email(
                            emailId = 1,
                            settingName = "email",
                            settingValue = event.settingValue
                        )
                    )
                }
            }

//           is AppViewEvent.OnGetSettingValue -> {
//               viewModelScope.launch {
//                   repository.getSettingValue(settingName)
//                   return@launch
//               }
//           }
        }
    }

    fun getSettingValue(settingName: String): Long = runBlocking {
        repository.getSettingValue(settingName)
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun handleImageCapture(uri: Uri) {
        // PHOTO TAKEN SOUND
        try {
            val sound = MediaActionSound()
            sound.play(MediaActionSound.SHUTTER_CLICK)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.i("kilo", "Image captured: $uri")
//        shouldShowCamera.value = false

        photoUri = uri
        shouldShowPhoto.value = true


    }

    private fun takePhotos(
        filenameFormat: String,
        imageCapture: ImageCapture,
        outputDirectory: File,
        executor: Executor,
        onImageCaptured: (Uri) -> Unit,
        onError: (ImageCaptureException) -> Unit,

        howMany: Int,
        delayMilliseconds: Long,
        baner: Int,
        email: String = ""
    ) {

        viewModelScope.launch {

            // START TAKING PHOTOS (AFTER BUTTON PRESS)
            try {
                val sound = MediaActionSound()
                sound.play(MediaActionSound.START_VIDEO_RECORDING)
//                val notification: Uri =
//                    RingtoneManager.getDefaultUri(RingtoneManager.)
//                val r = RingtoneManager.getRingtone(applicationContext, notification)
//                r.play()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            delay(delayMilliseconds)

            // PHOTO SERIES LOOP
            for (i in 0..howMany) {

                if(i <= howMany - 1) {




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

                            val ei = ExifInterface(photoFile.absolutePath)
                            val orientation: Int = ei.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED
                            )
                            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

                            var rotatedBitmap: Bitmap? = null
                            when (orientation) {
                                ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap =
                                    rotateImage(bitmap, 90F)
                                ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap =
                                    rotateImage(bitmap, 180F)
                                ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap =
                                    rotateImage(bitmap, 270F)
                                ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
                                else -> rotatedBitmap = bitmap
                            }

                            var fOut: FileOutputStream
                            try {
                                fOut = FileOutputStream(photoFile.absolutePath)
                                rotatedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
                                fOut.flush()
                                fOut.close()
                            } catch (e1: FileNotFoundException) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace()
                            } catch (e: IOException) {
                                // TODO Auto-generated catch block
                                e.printStackTrace()
                            }


                            val savedUri = Uri.fromFile(photoFile)

                            images.add(photoFile)
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

            // AFTER TAKING ALL PHOTOS
            try {
                val sound = MediaActionSound()
                sound.play(MediaActionSound.STOP_VIDEO_RECORDING)

                for (image in images) {
                    Log.d("gromzi", image.toString())
                }

                shouldShowPopup.value = true

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onPopupPressPdf(baner: Int) {
            uploadImages(images, baner, "")

            images.clear()
            shouldShowPopup.value = false

    }
    fun onPopupPressEmail(baner: Int) {
        viewModelScope.launch {
            val email = repository.getEmailById(1);

            uploadImages(images, baner, email!!.settingValue)
            Log.d("gromzi mail", email.settingValue)

            images.clear()
            shouldShowPopup.value = false
        }

    }

    private fun uploadImages(images: List<File>, baner: Int, email: String) {
        viewModelScope.launch {

            val result = repository.uploadImages(images, baner, email)
            if(email == ""){
                val downloader = AndroidDownloader(applicationContext)
                downloader.downloadFile("http://dom.webitup.pl/storage/${result!!.result!!.pdf!!}")
            }


        }
    }


    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
}