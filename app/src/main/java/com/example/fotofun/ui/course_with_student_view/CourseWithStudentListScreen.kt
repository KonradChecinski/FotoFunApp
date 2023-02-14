package com.example.fotofun.ui.course_with_student_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
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
import com.example.fotofun.data.entities.Course
import com.example.fotofun.data.entities.Student
import com.example.fotofun.data.relations.CourseWithStudentsWithGrades
import com.example.fotofun.data.relations.StudentWithGrades
import com.example.fotofun.util.Routes
import com.example.fotofun.util.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun CourseWithStudentListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    modifier: Modifier = Modifier,
    courseId: Int,
    viewModel: CourseWithStudentListViewModel = hiltViewModel()
) {

    val iconSize = 36.dp
    val offsetInPx = LocalDensity.current.run { (iconSize / 2).roundToPx() }

    val course = viewModel.course.collectAsState(initial = CourseWithStudentsWithGrades(
        Course(0,"","","", ""),
        listOf<StudentWithGrades>( StudentWithGrades(
            Student(null,"", "", ""),
            emptyList()
        )

        )
    )
    )

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
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
                    onNavigate(UiEvent.Navigate(route = Routes.COURSE_STUDENT_LIST_ADD_EDIT + "/" + course.value.course.courseId))
                }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Add student to course"
                )
            }
        }
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            Text(
                text = course.value.course.courseName,
                modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 40.sp
            )
            Text(
                text = "Studenci: ",
                modifier
                    .fillMaxWidth()
                    .padding(5.dp, 0.dp, 0.dp, 10.dp),
                textAlign = TextAlign.Left,
                fontSize = 15.sp
            )
            LazyColumn(
                modifier.fillMaxSize()
            ) {
                items(items = course.value.students){
                    Box{
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable {
//                                    viewModel.onEvent(CourseWithStudentListEvent.OnStudentClick(it))
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
                                    Text(text = it.student.name + " " + it.student.lastName, modifier.padding(1.dp, 5.dp, 0.dp, 1.dp))
                                    Text(text = "Numer albumu: " + it.student.albumNumber, modifier.padding(1.dp,0.dp, 5.dp, 0.dp))
                                    Text(text = "Oceny: ", modifier.padding(1.dp,0.dp, 5.dp, 0.dp))

                                    LazyRow(
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .padding(0.dp, 0.dp, 60.dp, 0.dp)
                                    ) {
                                        items(items = it.grades.filter{ grade -> grade.classId == courseId }){
                                            Card(
                                                modifier = modifier
                                                    .padding(0.dp, 10.dp, 10.dp, 10.dp)
                                                    .clickable { onNavigate(UiEvent.Navigate(route = Routes.GRADE_ADD_EDIT + "?gradeId=${it.gradeId}")) },
                                                elevation = 3.dp,
                                                shape = CutCornerShape(5.dp)
                                            ) {
                                                if(it.isPoints){
                                                    Text(text = it.points.toString(), modifier = modifier.padding(10.dp))
                                                }
                                                else{
                                                    Text(text = it.gradeValue.toString(), modifier = modifier.padding(10.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        IconButton(
                            onClick = {
                                onNavigate(UiEvent.Navigate(route = Routes.GRADE_ADD_EDIT + "?courseId=$courseId" + "&" + "studentId=${it.student.studentId}"))
                            },
                            modifier = Modifier
                                .offset {
                                    IntOffset(x = -offsetInPx, y = -offsetInPx)
                                }
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.background)
                                .size(iconSize)
                                .align(Alignment.BottomEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add grade",
                            )
                        }
                    }

                }
            }
        }
    }


}