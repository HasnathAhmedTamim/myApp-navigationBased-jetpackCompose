package com.example.myapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.myapp.components.AlbumItemCard      // ✅ Add this
import com.example.myapp.components.AppBottomNav
import com.example.myapp.components.AppTopBar
import com.example.myapp.components.BottomNavItem
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
    onAlbumItemClick: (AlbumItem) -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "My App",
                showProfileButton = true,
                onProfileClick = onProfileClick
            )
        },
        bottomBar = {
            AppBottomNav(
                currentScreen = BottomNavItem.ALBUM,
                onHomeClick = onHomeClick,
                onSearchClick = onSearchClick,
                onAlbumClick = onAlbumClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header with button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Album Items",
                    style = MaterialTheme.typography.headlineMedium
                )
                Button(onClick = onAlbumDetailClick) {
                    Text("View All")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Album items list - ✅ Using reusable component
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(albumItems) { item ->
                    AlbumItemCard(
                        item = item,
                        onClick = { onAlbumItemClick(item) }
                    )
                }
            }
        }
    }
}
