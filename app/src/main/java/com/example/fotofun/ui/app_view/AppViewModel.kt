package com.example.fotofun.ui.app_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.ui.camera_view.CameraViewEvent
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: AssistantRepository
): ViewModel() {

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CameraViewEvent) {
        when(event) {

        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}