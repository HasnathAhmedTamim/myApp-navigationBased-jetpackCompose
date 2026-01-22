package com.example.myapp.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.myapp.model.AlbumItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    items: List<AlbumItem>,
    onBack: () -> Unit,
    onItemClick: (AlbumItem) -> Unit
){
    //    Todo
}