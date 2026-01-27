package com.example.myapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.components.AppTopBar

/**
 * Card Detail Screen
 *
 * Shows detailed information about a selected card from HomeScreen.
 * Uses reusable AppTopBar with back button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    cardId: String,
    cardTitle: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = cardTitle,           // ✅ Dynamic title (card name)
                showBackButton = true,       // ✅ Show back button
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
            Text(
                text = "Card Details",
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
                        text = "ID: $cardId",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Title: $cardTitle",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "This is a detailed view of the selected card.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
