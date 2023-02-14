package com.example.fotofun.ui.drawer_menu

import androidx.compose.material.*
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = {
//            Text(text = stringResource(R.string.app_name))
        },
//        backgroundColor = MaterialTheme.colors.primary,
//        contentColor = MaterialTheme.colors.onPrimary,
//        backgroundColor = Color(R.color.transparent),
//        contentColor = Color(R.color.transparent),
        backgroundColor = Color.Transparent.copy(alpha = 0.01f),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer"
                )
            }
        }
    )
}