package com.example.fotofun.ui.add_edit_grade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
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
fun AddEditGradeScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditGradeViewModel = hiltViewModel(),
    gradeId: Int,
    studentId: Int?,
    courseId: Int?
) {
    val scaffoldState = rememberScaffoldState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("2", "2.5", "3", "3.5", "4", "4.5", "5")
    var textfieldSize by remember { mutableStateOf(Size.Zero)}


    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(AddEditGradeEvent.OnConfirmDeleteGradeClick)
                    }
                }
                is UiEvent.PopBackStack -> onPopBackStack()
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
            if(gradeId!=-1){
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ){
                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        onClick = {
                            viewModel.onEvent(AddEditGradeEvent.OnDeleteGradeClick)
                        }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Save"
                        )
                    }

                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        onClick = {
                            viewModel.onEvent(AddEditGradeEvent.OnSaveGradeClick)
                        }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.save),
                            contentDescription = "Save"
                        )
                    }
                }
            }else{
                FloatingActionButton(
                    backgroundColor = MaterialTheme.colors.primary,
                    onClick = {
                        viewModel.onEvent(AddEditGradeEvent.OnSaveGradeClick)
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.save),
                        contentDescription = "Save"
                    )
                }
            }


        },
        floatingActionButtonPosition = if(gradeId!=-1) FabPosition.Center else FabPosition.End,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = if (gradeId == -1) "Dodaj ocenę" else "Edytuj ocenę",
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 40.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row() {
                Text("Punkty zamiast oceny ")
                Checkbox(
                    checked = viewModel.isPoints,
                    onCheckedChange = {
                        viewModel.onEvent(AddEditGradeEvent.OnCheckBoxClick(!viewModel.isPoints))
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .size(25.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if(viewModel.isPoints){
                OutlinedTextField(
                    value =  if(viewModel.points != null) viewModel.points.toString() else "",
                    onValueChange = { viewModel.onEvent(AddEditGradeEvent.OnPointsChange(it)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text("Punkty") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )
            }else{
                Column() {
                    OutlinedTextField(
                        value = if(viewModel.gradeValue != null) viewModel.gradeValue.toString() else "",
                        onValueChange = { viewModel.onEvent(AddEditGradeEvent.OnGradeChange(it)) },
                        readOnly=true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                //This value is used to assign to the DropDown the same width
                                textfieldSize = coordinates.size.toSize()
                            }
                            .clickable { expanded = !expanded },
                        label = { Text("Ocena") },
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
                            viewModel.onEvent(AddEditGradeEvent.OnGradeChange(label))
                                expanded = false
                            }) {
                                Text(text = label)
                            }
                        }
                    }
                }
            }
        }
    }
}