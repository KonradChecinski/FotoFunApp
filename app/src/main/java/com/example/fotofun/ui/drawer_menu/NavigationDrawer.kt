package com.example.fotofun.ui.drawer_menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fotofun.ui.app_view.AppViewEvent
import com.example.fotofun.ui.app_view.AppViewModel
import kotlin.jvm.internal.FunctionReference

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "FotoFun", fontSize = 60.sp)
    }
}

@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel()
) {

    val suggestionsHowManyPhotos = listOf("5", "6")
    val suggestionsDelay = listOf("1", "2", "3", "4", "5")
    val suggestionsBanner = listOf("1", "2", "3", "4")




    Dropdown(
        suggestions = suggestionsHowManyPhotos,
        labelParam = "Ilość zdjęć",
        valueParam =  viewModel.getSettingValue("photosQuantity").toString(),
    )

    Dropdown(
        suggestions = suggestionsDelay,
        labelParam = "Opóźnienie (sekundy)",
        valueParam = viewModel.getSettingValue("photosDelay").toString().slice(listOf(0))
    )

    Dropdown(
        suggestions = suggestionsBanner,
        labelParam = "Baner",
        valueParam = viewModel.getSettingValue("banner").toString()
    )

}

@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    viewModel: AppViewModel = hiltViewModel(),
    suggestions: List<String>,
    labelParam: String,
    valueParam: String
) {

    var expanded by remember { mutableStateOf(false) }
    var textfieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column() {
        OutlinedTextField(
            value = valueParam,
            onValueChange =
            {
                when (labelParam) {
                    "Ilość zdjęć" -> {
                        viewModel.onEvent(AppViewEvent.OnSetPhotosQuantity(it.toLong()))
                    }
                    "Opóźnienie (sekundy)" -> {
                        viewModel.onEvent(AppViewEvent.OnSetDelay((it + "000").toLong()))
                    }
                    else -> viewModel.onEvent(AppViewEvent.OnSetBanner(it.toLong()))
                }
            },
            readOnly=true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp)
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .clickable { expanded = !expanded },
            label = { Text(labelParam) },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
//                .width(100.dp)
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() + 40.dp })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {

                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(50.dp))
}