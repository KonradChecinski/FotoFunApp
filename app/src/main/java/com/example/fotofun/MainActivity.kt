package com.example.fotofun

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.system.Os
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.example.fotofun.ui.add_edit_course.AddEditCourseScreen
import com.example.fotofun.ui.add_edit_course_with_student_view.AddEditCourseWithStudentScreen
import com.example.fotofun.ui.add_edit_grade.AddEditGradeScreen
import com.example.fotofun.ui.add_edit_student.AddEditStudentScreen
import com.example.fotofun.ui.app_view.AppView
import com.example.fotofun.ui.clear_db_view.ClearDBScreen
import com.example.fotofun.ui.course_with_student_view.CourseWithStudentListScreen
import com.example.fotofun.ui.courses_view.CoursesListScreen
import com.example.fotofun.ui.drawer_menu.AppBar
import com.example.fotofun.ui.drawer_menu.DrawerBody
import com.example.fotofun.ui.drawer_menu.DrawerHeader
import com.example.fotofun.ui.drawer_menu.MenuItem
import com.example.fotofun.ui.students_view.StudentsListScreen
import com.example.fotofun.ui.theme.AssistantAppTheme
import com.example.fotofun.util.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)
    private lateinit var outputDirectory: File


    // region REQUEST PERMISSION LAUNCHER
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted) {
            Log.i("kilo", "Permission granted")
            shouldShowCamera.value = true
        } else {
            Log.i("kilo", "Permission denied")
        }
    }
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("kilo", "Permission previously granted")
                shouldShowCamera.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            ) -> {
                Log.i("kilo", "Show camera permissions dialog")
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }

            else -> requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    // endregion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssistantAppTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        AppBar(
                            onNavigationIconClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        )
                    },
//                    backgroundColor = Color(R.color.transparent),
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
                        DrawerHeader()
                        DrawerBody(
                            items = listOf(
                                MenuItem(
                                    id = "zajecia",
                                    title = "Zajęcia",
                                    contentDescription = "Pokaż widok zajęć",
                                    icon = ImageVector.vectorResource(id = R.drawable.school),
                                    route = Routes.COURSES_LIST
                                ),
                                MenuItem(
                                    id = "studenci",
                                    title = "Studenci",
                                    contentDescription = "Pokaż widok studenta",
                                    icon = ImageVector.vectorResource(id = R.drawable.group),
                                    route = Routes.STUDENTS_LIST
                                ),
                            ),
                            onItemClick = {
                               navController.navigate(it.route)
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        DrawerBody(
                            items = listOf(
                                MenuItem(
                                    id = "db",
                                    title = "Wyczyść bazę danych",
                                    contentDescription = "Wyczyść bazę danych",
                                    icon = ImageVector.vectorResource(id = R.drawable.database),
                                    route = Routes.CLEAR_DB
                                )
                            ),
                            onItemClick = {
                                navController.navigate(it.route)
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                        )
                    }
                ) {
                    Box {
                        NavHost(
                            navController = navController,
                            startDestination = Routes.APP_VIEW,
                            ) {


                            // region WIDOK GŁÓWNY APLIKACJI
                            composable(route = Routes.APP_VIEW) {
                                AppView(
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    },
                                    cameraExecutor = cameraExecutor,
                                    shouldShowCamera = shouldShowCamera,
                                    outputDirectory = outputDirectory
                                )
                            }

                            // endregion

                            //region Student

                            //region Studenci lista wszystkich

                            composable(route = Routes.STUDENTS_LIST) {
                                StudentsListScreen(
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    }
                                )
                            }
                        //endregion

                            //region Studenci na zajęciach
//                            composable(
//                                route = Routes.STUDENT_LIST + "?classID={classID}",
//                                arguments = listOf(
//                                    navArgument("classID") {
//                                        type = NavType.StringType
////                                        defaultValue = "ID ZAJĘĆ DOMYŚLNY"
//                                        nullable = true
//                                    }
//                                )
//                            ) { entry ->
//                                StudentsListScreen(
//                                    onNavigate = {
//                                        navController.navigate(it.route)
//                                    },
//                                    classID = entry.arguments?.getString("classID")
//                                )
//                            }
                            //endregion

                            //region Widok Edycji/Dodawania Studentow
                            composable(
                                route = Routes.STUDENT_ADD_EDIT + "?studentId={studentId}",
                                arguments = listOf(
                                    navArgument(name = "studentId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    }
                                )
                            ) {entry ->
                                AddEditStudentScreen(
                                    onPopBackStack = {
                                        navController.popBackStack()
                                    },
                                    studentId = entry.arguments!!.getInt("studentId")
                                )
                            }
                            //endregion

                            //endregion

                            //region Grade

                            //region Edytowanie lub dodawania ocen lub punktów
                            composable(
                                route = Routes.GRADE_ADD_EDIT + "?gradeId={gradeId}&courseId={courseId}&studentId={studentId}",
                                arguments = listOf(
                                    navArgument(name = "gradeId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    },
                                    navArgument(name = "courseId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    },
                                    navArgument(name = "studentId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    }

                                )
                            ) {entry ->
                                AddEditGradeScreen(
                                    onPopBackStack = {
                                        navController.popBackStack()
                                    },
                                    gradeId = entry.arguments!!.getInt("gradeId"),
                                    courseId = entry.arguments!!.getInt("gradeId"),
                                    studentId = entry.arguments!!.getInt("gradeId")
                                )
                            }
                           //endregion

                            //endregion

                            //region Course

                            //region widok wszystkich przedmiotów
                            composable(route = Routes.COURSES_LIST) {
                                CoursesListScreen(
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    }
                                )
                            }

                            //endregion

                            //region Widok edycji / dodawania przedmiotów
                            composable(
                                route = Routes.COURSE_ADD_EDIT + "?courseId={courseId}",
                                arguments = listOf(
                                    navArgument(name = "courseId") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    }
                                )
                            ) {entry ->
                                AddEditCourseScreen(
                                    onPopBackStack = {
                                        navController.popBackStack()
                                    },
                                    courseId = entry.arguments!!.getInt("courseId")
                                )
                            }

                            //endregion

                            //region widok Jednego przedmiotu z studentami
                            composable(
                                route = Routes.COURSE_STUDENT_LIST + "/{courseId}",
                                arguments = listOf(
                                    navArgument(name = "courseId") {
                                        type = NavType.IntType
                                    }
                                )
                            ) {entry ->
                                CourseWithStudentListScreen(
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    },
                                    courseId = entry.arguments!!.getInt("courseId")
                                )
                            }

                            //endregion

                            //region widok dodania studenta do przedmiotu
                            composable(
                                route = Routes.COURSE_STUDENT_LIST_ADD_EDIT + "/{courseId}",
                                arguments = listOf(
                                    navArgument(name = "courseId") {
                                        type = NavType.IntType
                                    }
                                )
                            ) {entry ->
                                AddEditCourseWithStudentScreen(
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    },
                                    courseId = entry.arguments!!.getInt("courseId")
                                )
                            }

                            //endregion

                            //endregion


                            //region Clear DB
                            composable(route = Routes.CLEAR_DB) {
                                ClearDBScreen(
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    }
                                )
                            }
                            //endregion

                        }
                    }
                }
            }
        }

        requestCameraPermission()
        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDirectory = getOutputDirectory()
    }
}