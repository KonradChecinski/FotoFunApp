package com.example.fotofun.ui.add_edit_course

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fotofun.R
import com.example.fotofun.util.UiEvent
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddEditCourseScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditCourseViewModel = hiltViewModel(),
    courseId: Int
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela")
    var textfieldSize by remember { mutableStateOf(Size.Zero)}


    val timePickerDialogFrom = TimePickerDialog(
        LocalContext.current,
        {_, hour:Int, minute: Int->
            viewModel.onEvent(AddEditCourseEvent.OnTimeFromBlockChange(checkDigit(hour)+":"+checkDigit(minute)))
        }, 0, 0, true
    )
    val timePickerDialogTo = TimePickerDialog(
        LocalContext.current,
        {_, hour:Int, minute: Int->
            viewModel.onEvent(AddEditCourseEvent.OnTimeToBlockChange(checkDigit(hour)+":"+checkDigit(minute)))
        }, 0, 0, true
    )

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }
                else -> Unit
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                onClick = {
                viewModel.onEvent(AddEditCourseEvent.OnSaveCourseClick)
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.save),
                    contentDescription = "Save"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = if (courseId == -1) "Dodaj przedmiot" else "Edytuj przedmiot",
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 40.sp
            )
            TextField(
                value = viewModel.courseName,
                onValueChange = {
                    viewModel.onEvent(AddEditCourseEvent.OnCourseNameChange(it))
                },
                placeholder = {
                    Text(text = "Nazwa przedmiotu")
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column() {
                OutlinedTextField(
                    value = viewModel.weekDay,
                    onValueChange = { viewModel.onEvent(AddEditCourseEvent.OnWeekDayChange(it)) },
                    readOnly=true,

//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
//                        backgroundColor = TextFieldDefaults.textFieldColors().backgroundColor(enabled = true).value.copy(),
//                        disabledBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
//                        disabledLabelColor = MaterialTheme.colors.primary,
//                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textfieldSize = coordinates.size.toSize()
                        }
                        .clickable { expanded = !expanded },
                    label = { Text("Dzień tygodnia") },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
                ) {
                    suggestions.forEach { label ->
                        DropdownMenuItem(onClick = {
                            viewModel.onEvent(AddEditCourseEvent.OnWeekDayChange(label))
                            expanded = false
                        }) {
                            Text(text = label)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = viewModel.timeBlockFrom,
                    onValueChange = {
                        viewModel.onEvent(AddEditCourseEvent.OnTimeFromBlockChange(it))
                    },
                    placeholder = {
                        Text(text = "00:00")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                        disabledLabelColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                    ),
                    enabled = false,
                    modifier = Modifier.width(100.dp).clickable { timePickerDialogFrom.show() },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )
                Text(
                    text = "-",
                )
                TextField(
                    value = viewModel.timeBlockTo,
                    onValueChange = {
                        viewModel.onEvent(AddEditCourseEvent.OnTimeToBlockChange(it))
                    },
                    placeholder = {
                        Text(text = "00:00")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                        disabledLabelColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                    ),
                    enabled = false,
                    modifier = Modifier.width(100.dp).clickable { timePickerDialogTo.show() },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )
            }

        }
    }
}

private fun checkDigit(number: Int): String {
    return if(number <= 9) "0" + number.toString() else number.toString();
}