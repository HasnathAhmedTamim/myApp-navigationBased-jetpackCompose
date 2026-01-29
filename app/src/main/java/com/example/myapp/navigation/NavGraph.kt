package com.example.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myapp.model.AlbumItem
import com.example.myapp.model.User
import com.example.myapp.screens.AlbumDetailScreen
import com.example.myapp.screens.AlbumItemDetailScreen
import com.example.myapp.screens.AlbumScreen
import com.example.myapp.screens.CardDetailScreen
import com.example.myapp.screens.HomeScreen
import com.example.myapp.screens.auth.LoginScreen
import com.example.myapp.screens.auth.SignupScreen
import com.example.myapp.screens.auth.ForgotPasswordScreen
import com.example.myapp.screens.auth.OtpVerificationScreen
import com.example.myapp.screens.auth.ResetPasswordScreen
import com.example.myapp.screens.ProfileScreen
import com.example.myapp.screens.SearchScreen
import com.example.myapp.viewmodel.AuthViewModel


@Composable
fun NavGraph(authViewModel: AuthViewModel) {
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
                onLoginSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo<Screen.Login> { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(Screen.Signup)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword)
                },
                viewModel = authViewModel
            )
        }

        // SignupScreen route
        composable<Screen.Signup> {
            SignupScreen(
                onSignupSuccess = {
                    // After signup, go to login or OTP screen as needed
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = authViewModel
            )
        }

        // ForgotPasswordScreen route
        composable<Screen.ForgotPassword> {
            ForgotPasswordScreen(
                onPhoneVerified = { username, phoneNumber ->
                    navController.navigate(Screen.OtpVerification(username, phoneNumber))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = authViewModel
            )
        }
        // OtpVerificationScreen route
        composable<Screen.OtpVerification> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.OtpVerification>()
            OtpVerificationScreen(
                username = args.username,
                phoneNumber = args.phoneNumber,
                isForSignup = false,
                onOtpVerified = {
                    navController.navigate(Screen.ResetPassword(args.username, args.phoneNumber))
                },
                onBackClick = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }
        // ResetPasswordScreen route
        composable<Screen.ResetPassword> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ResetPassword>()
            ResetPasswordScreen(
                username = args.username,
                phoneNumber = args.phoneNumber,
                onPasswordReset = {
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }

        //HomeScreen route
        composable<Screen.Home> {
            // Fetch user info from Room after login
            androidx.compose.runtime.LaunchedEffect(Unit) {
                val user = authViewModel.getCurrentUserModel()
                if (user != null) {
                    currentUser.value = user
                }
            }
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
                onBack = { navController.popBackStack() },
                viewModel = authViewModel,
                onLogout = {
                    currentUser.value = null
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        //SearchScreen route
        composable<Screen.Search> {
            SearchScreen(
                username = currentUser.value?.username ?: "User",
                onProfileClick = { navController.navigate(Screen.Profile) },
                onHomeClick = { navController.navigate(Screen.Home) { popUpTo<Screen.Home> { inclusive = true } } },
                onSearchClick = { },
                onAlbumClick = { navController.navigate(Screen.Album) { popUpTo<Screen.Home>() } },
                onCardClick = { cardId, cardTitle -> navController.navigate(Screen.CardDetail(cardId, cardTitle)) }
            )

        }
        //AlbumScreen route
        composable<Screen.Album> {
            AlbumScreen(
                albumItems = albumItems,
                onBack = { navController.popBackStack() },  // Kept for compatibility
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
                onAlbumClick = { }, // Stay on current screen
                onAlbumDetailClick = {
                    navController.navigate(Screen.AlbumDetail)
                },
                onAlbumItemClick = { item ->
                    navController.navigate(
                        Screen.AlbumItemDetail(item.id, item.title)
                    )
                },
                onProfileClick = {                           // ✅ Add this
                    navController.navigate(Screen.Profile)
                }
            )
        }


        //AlbumDetailScreen route
        composable<Screen.AlbumDetail> {
            AlbumDetailScreen(
                items = albumItems,
                onBack = { navController.popBackStack() },
                onItemClick = { item ->
                    navController.navigate(
                        Screen.AlbumItemDetail(item.id, item.title)
                    )
                }
            )
        }

        //AlbumItemDetailScreen route
        composable<Screen.AlbumItemDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.AlbumItemDetail>()
            val item = albumItems.find { it.id == args.itemId }

            AlbumItemDetailScreen(
                item = item,
                onBack = { navController.popBackStack() }
            )
        }

        //
        composable<Screen.CardDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.CardDetail>()

            CardDetailScreen(
                cardId = args.cardId,
                cardTitle = args.cardTitle,
                onBack = { navController.popBackStack() }
            )
        }


    }
}