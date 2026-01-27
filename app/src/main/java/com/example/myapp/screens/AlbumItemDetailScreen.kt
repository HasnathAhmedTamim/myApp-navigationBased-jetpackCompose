package com.example.myapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.components.AppTopBar
import com.example.myapp.model.AlbumItem

/**
 * Album Item Detail Screen
 *
 * Shows detailed information about a specific album item.
 * Uses reusable AppTopBar with back button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumItemDetailScreen(
    item: AlbumItem?,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = item?.title ?: "Item Detail",  // ✅ Dynamic title
                showBackButton = true,                 // ✅ Show back button
                onBackClick = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (item != null) {
                Text(
                    text = "Album Item Details",
                    style = MaterialTheme.typography.headlineMedium
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "ID: ${item.id}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Title: ${item.title}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Description: ${item.description}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                Text("Item not found")
            }
        }
    }
}
