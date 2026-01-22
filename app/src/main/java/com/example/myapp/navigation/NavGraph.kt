package com.example.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.model.AlbumItem
import com.example.myapp.model.User
import com.example.myapp.screens.AlbumScreen
import com.example.myapp.screens.HomeScreen
import com.example.myapp.screens.LoginScreen
import com.example.myapp.screens.ProfileScreen
import com.example.myapp.screens.SearchScreen


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
        // LoginScreen route
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = { username, password ->
                    currentUser.value = User(
                        username = username,
                        email = "$username@example.com",
                        phone = "01712345678"
                    )
                    navController.navigate(Screen.Home) {
                        popUpTo<Screen.Login> { inclusive = true }
                    }
                }
            )
        }

        //HomeScreen route
        composable<Screen.Home> {
            HomeScreen(
                username = currentUser.value?.username ?: "User",
                onProfileClick = {
                    navController.navigate(Screen.Profile)
                },
                onHomeClick = { },
                onSearchClick = {
                    navController.navigate(Screen.Search)
                },
                onAlbumClick = {
                    navController.navigate(Screen.Album)
                },
                onCardClick = { cardId, cardTitle ->
                    navController.navigate(
                        Screen.CardDetail(cardId, cardTitle)
                    )
                }
            )
        }

        //ProfileScreen route
        composable<Screen.Profile> {
            ProfileScreen(
                user = currentUser.value,
                onBack = { navController.popBackStack() }
            )
        }

        //SearchScreen route
        composable<Screen.Search> {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onHomeClick = {
                    navController.navigate(Screen.Home) {
                        popUpTo<Screen.Home> { inclusive = true }
                    }
                },
                onSearchClick = { },
                onAlbumClick = {
                    navController.navigate(Screen.Album) {
                        popUpTo<Screen.Home>()
                    }
                }
            )
        }
        //AlbumScreen route
        composable<Screen.Album> {
            AlbumScreen(
                albumItems = albumItems,
                onBack = { navController.popBackStack() },
                onHomeClick = {
                    navController.navigate(Screen.Home) {
                        popUpTo<Screen.Home> { inclusive = true }
                    }
                },
                onSearchClick = {
                    navController.navigate(Screen.Search) {
                        popUpTo<Screen.Home>()
                    }
                },
                onAlbumClick = { },
                onAlbumDetailClick = {
                    navController.navigate(Screen.AlbumDetail)
                },
                onAlbumItemClick = { item ->
                    navController.navigate(
                        Screen.AlbumItemDetail(item.id, item.title)
                    )
                }
            )
        }

    }
}