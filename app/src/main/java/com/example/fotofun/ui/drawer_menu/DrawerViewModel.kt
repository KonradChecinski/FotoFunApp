package com.example.fotofun.ui.drawer_menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.fotofun.data.AssistantRepository
import com.example.fotofun.data.FotoFunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val repository: FotoFunRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {


}