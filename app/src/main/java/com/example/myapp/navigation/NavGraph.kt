package com.example.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.myapp.model.AlbumItem
import com.example.myapp.model.User


@Composable
fun NavGraph() {
    // Navigation graph implementation goes here
    val navController = rememberNavController()
    val currentUser = remember { mutableStateOf<User?>(null) }

    val albumItems = listOf(
        AlbumItem("1", "Album Item 1", "Description for item 1"),
        AlbumItem("2", "Album Item 2", "Description for item 2"),
        AlbumItem("3", "Album Item 3", "Description for item 3"),
        AlbumItem("4", "Album Item 4", "Description for item 4"),
        AlbumItem("5", "Album Item 5", "Description for item 5")
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Login
    ){


    }
}