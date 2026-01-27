package com.example.myapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.components.AlbumItemCard      // ✅ Add this
import com.example.myapp.components.AppTopBar
import com.example.myapp.model.AlbumItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    items: List<AlbumItem>,
    onBack: () -> Unit,
    onItemClick: (AlbumItem) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "All Albums",
                showBackButton = true,
                onBackClick = onBack
            )
        }
    ) { paddingValues ->
        // ✅ Using reusable component
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                AlbumItemCard(
                    item = item,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}
