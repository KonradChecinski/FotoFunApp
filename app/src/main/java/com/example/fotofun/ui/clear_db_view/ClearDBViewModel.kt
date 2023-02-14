package com.example.fotofun.ui.clear_db_view

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotofun.data.AssistantDatabase
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClearDBViewModel @Inject constructor(
    private val repository: AssistantRepository,
    private val db: AssistantDatabase
): ViewModel() {
    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var isCleared by mutableStateOf(false)
        private set

    fun onEvent(event: ClearDBEvent) {
        when(event) {
            is ClearDBEvent.OnDeleteDBClick -> {
                Log.d("cos", "t")
                viewModelScope.launch {
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Czy jesteś pewny?",
                        action = "Tak"
                    ))
                }
            }
            is ClearDBEvent.OnConfirmDeleteDBClick -> {
                CoroutineScope(Dispatchers.IO).launch {
                    db.clearAllTables()
                    isCleared=true
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}