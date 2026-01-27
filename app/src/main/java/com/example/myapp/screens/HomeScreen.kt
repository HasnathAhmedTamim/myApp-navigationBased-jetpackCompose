package com.example.myapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.components.AppBottomNav
import com.example.myapp.components.AppTopBar
import com.example.myapp.components.BottomNavItem

/**
 * Home Screen - Main Landing Page
 *
 * Features:
 * - Top bar with app name and profile icon
 * - Bottom navigation (Home, Search, Album)
 * - 2x2 grid of clickable cards
 *
 * @param username Current logged-in username
 * @param onProfileClick Navigate to profile
 * @param onHomeClick Stay on home (no action)
 * @param onSearchClick Navigate to search
 * @param onAlbumClick Navigate to album
 * @param onCardClick Navigate to card detail (cardId, cardTitle)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAlbumClick: () -> Unit,
    onCardClick: (String, String) -> Unit
) {
    Scaffold(
        // ========================================
        // TOP APP BAR - REUSABLE COMPONENT
        // ========================================
        topBar = {
            AppTopBar(
                title = "My App",
                showProfileButton = true,
                onProfileClick = onProfileClick
            )
        },

        // ========================================
        // BOTTOM NAVIGATION BAR - REUSABLE COMPONENT
        // ========================================
        bottomBar = {
            AppBottomNav(
                currentScreen = BottomNavItem.HOME,
                onHomeClick = onHomeClick,
                onSearchClick = onSearchClick,
                onAlbumClick = onAlbumClick
            )
        }
    ) { paddingValues ->

        // ========================================
        // 2x2 GRID CARDS
        // ========================================
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(4) { index ->
                GridCard(
                    cardNumber = index + 1,
                    onClick = {
                        onCardClick("${index + 1}", "Card ${index + 1}")
                    }
                )
            }
        }
    }
}

/**
 * Grid Card Component
 * Reusable card for the 2x2 grid
 */
@Composable
private fun GridCard(
    cardNumber: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Card $cardNumber",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
