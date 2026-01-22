package com.example.myapp.screens
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Album") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = onHomeClick
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, "Search") },
                    label = { Text("Search") },
                    selected = false,
                    onClick = onSearchClick
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.PhotoAlbum, "Album") },
                    label = { Text("Album") },
                    selected = true,
                    onClick = onAlbumClick
                )
            }
        }
    ){padding->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ){
            item {
                Button(
                    onClick = onAlbumDetailClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Album Details")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(albumItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAlbumItemClick(item) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.title)
                        Text(item.description)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}