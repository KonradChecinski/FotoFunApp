package com.example.fotofun.ui.drawer_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fotofun.R
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val suggestionsHowManyPhotos = listOf("4", "5", "6")
    val suggestionsDelay = listOf("1", "2", "3", "4", "5")
    val suggestionsBanner = listOf("baner1", "baner2", "baner3", "baner4", "baner5")

    val settings = viewModel.settings.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 0.dp, 20.dp, 50.dp)
    ) {
        Text(text = "Email")
        TextField(
            value = viewModel.email.value,
            onValueChange = { viewModel.onEvent(AppViewEvent.OnUpdateEmail(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email ,imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            )
        )
    }

    Dropdown(
        suggestions = suggestionsHowManyPhotos,
        labelParam = "Ilość zdjęć",
        valueParam = if(!settings.value.isNullOrEmpty() && settings.value.size >=1) settings.value[0].settingValue.toString() else ""
    )

    Dropdown(
        suggestions = suggestionsDelay,
        labelParam = "Opóźnienie (sekundy)",
        valueParam = if(!settings.value.isNullOrEmpty() && settings.value.size >=2) settings.value[1].settingValue.toString().slice(listOf(0)) else ""
    )

    Dropdown(
        suggestions = suggestionsBanner,
        labelParam = "Baner",
        valueParam = if(!settings.value.isNullOrEmpty() && settings.value.size >=3) settings.value[2].settingValue.toString() else ""
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
                    when (labelParam) {
                        "Ilość zdjęć" -> {
                            viewModel.onEvent(AppViewEvent.OnSetPhotosQuantity(label.toLong()))
                        }
                        "Opóźnienie (sekundy)" -> {
                            viewModel.onEvent(AppViewEvent.OnSetDelay((label + "000").toLong()))
                        }
                        else -> viewModel.onEvent(AppViewEvent.OnSetBanner(label.slice(listOf(5)).toLong()))
                    }
                    expanded = false
                }) {
                        if (labelParam != "Baner") {
                            Text(text = label)
                        }
                        else {
//                            val currentBanner = "R.drawable.${label}".toInt()
                            lateinit var image: Painter

                            when (label) {
                                "baner1" -> {
                                    image = painterResource(id = R.drawable.baner1)
                                }
                                "baner2" -> {
                                    image = painterResource(id = R.drawable.baner2)
                                }
                                "baner3" -> {
                                    image = painterResource(id = R.drawable.baner3)
                                }
                                "baner4" -> {
                                    image = painterResource(id = R.drawable.baner4)
                                }
                                "baner5" -> {
                                    image = painterResource(id = R.drawable.baner5)
                                }
                            }

                            Box(modifier = Modifier.padding(5.dp)) {
                                Image(painter = image, contentDescription = "Baner")
                            }

                        }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(50.dp))
}