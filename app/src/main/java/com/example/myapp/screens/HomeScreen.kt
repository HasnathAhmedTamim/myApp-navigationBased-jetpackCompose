package com.example.myapp.screens


import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAlbumClick: () -> Unit,
    onCardClick: (String, String) -> Unit
){
    //    Todo
}