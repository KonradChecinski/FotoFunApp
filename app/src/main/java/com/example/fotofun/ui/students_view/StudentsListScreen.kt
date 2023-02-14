package com.example.fotofun.ui.students_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fotofun.R
import com.example.fotofun.util.Routes
import com.example.fotofun.util.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun StudentsListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentsListViewModel = hiltViewModel()
) {
    val iconSize = 30.dp
    val offsetInPx = LocalDensity.current.run { (iconSize / 2).roundToPx() }

    val students = viewModel.students.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(StudentsListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> onNavigate(event)
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
                    onNavigate(UiEvent.Navigate(route = Routes.STUDENT_ADD_EDIT))
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add student"
                )
            }
        }
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            Text(
                text = "Studenci",
                modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 40.sp
            )
            LazyColumn(
                modifier.fillMaxSize()
            ) {
                items(items = students.value){
                    Box{
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable {
                                    viewModel.onEvent(StudentsListEvent.OnStudentClick(it))
                                },
                            elevation = 3.dp,
                            shape = RoundedCornerShape(10.dp),
                        ) {
                            Row(
                                modifier = modifier
                                    .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.person48),
                                    contentDescription = "Zapisz",
                                    modifier.padding(20.dp),
                                )
                                Column(
                                    modifier = modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Text(text = it.name + " " + it.lastName, modifier.padding(1.dp, 0.dp, 0.dp, 1.dp))
                                    Text(text = "Numer albumu: " + it.albumNumber, modifier.padding(1.dp,0.dp, 5.dp, 0.dp))
                                }
                            }
                        }
                        IconButton(
                            onClick = {viewModel.onEvent(StudentsListEvent.OnDeleteStudentClick(it))},
                            modifier = Modifier
                                .offset {
                                    IntOffset(x = -offsetInPx, y = +offsetInPx)
                                }
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.background)
                                .size(iconSize)
                                .align(Alignment.TopEnd)
                                .padding(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "",
                            )
                        }
                    }

                }
            }
        }
    }


}