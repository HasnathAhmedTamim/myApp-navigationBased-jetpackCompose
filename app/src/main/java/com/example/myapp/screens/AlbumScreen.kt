package com.example.myapp.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.myapp.model.AlbumItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    albumItems: List<AlbumItem>,
    onBack: () -> Unit,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAlbumClick: () -> Unit,
    onAlbumDetailClick: () -> Unit,
    onAlbumItemClick: (AlbumItem) -> Unit
){
    //    Todo
}