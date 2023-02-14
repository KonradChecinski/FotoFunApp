package com.example.fotofun.ui.add_edit_course_with_student_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fotofun.R
import com.example.fotofun.util.UiEvent

@Composable
fun AddEditCourseWithStudentScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
    courseId: Int,
    viewModel: AddEditCourseWithStudentViewModel = hiltViewModel()
) {
    val iconSize = 30.dp
    val offsetInPx = LocalDensity.current.run { (iconSize / 2).roundToPx() }

    val studentsWithCourse = viewModel.studentsWithCourse.collectAsState(initial = emptyList())

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            Text(
                text = "Studenci na zajęciach",
                modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
//            TextField(
//                value = viewModel.text,
//                onValueChange = {
//                    viewModel.onEvent(AddEditCourseWithStudentEvent.OnSearchBarChange(it))
//                                },
//                placeholder = {
//                    Text(text = "Znajdź studenta")
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(70.dp)
//                    .padding(0.dp, 0.dp, 0.dp, 15.dp)
//            )
            LazyColumn(
                modifier.fillMaxSize()
            ) {
                items(items = studentsWithCourse.value ){
                    studentWithCourse ->
                    Box{
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable {
                                    viewModel.onEvent(AddEditCourseWithStudentEvent.OnStudentClick(studentWithCourse))
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
                                    Text(text = studentWithCourse.student.name + " " + studentWithCourse.student.lastName, modifier.padding(1.dp, 0.dp, 0.dp, 1.dp))
                                    Text(text = "Numer albumu: " + studentWithCourse.student.albumNumber, modifier.padding(1.dp,0.dp, 5.dp, 0.dp))
                                }
                            }
                        }
                        Checkbox(
                            checked = studentWithCourse.courses.any{ course -> course.courseId == courseId},
                            onCheckedChange = {
                                viewModel.onEvent(AddEditCourseWithStudentEvent.OnStudentClick(studentWithCourse))
                            },
                            modifier = Modifier
                                .offset {
                                    IntOffset(x = -offsetInPx, y = 0)
                                }
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .size(iconSize)
                                .align(Alignment.CenterEnd)
                                .padding(5.dp)
                        )
                    }
                }
            }
        }
    }


}