package com.example.myapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Bottom Navigation Items
 */
enum class BottomNavItem {
    HOME,
    SEARCH,
    ALBUM
}

/**
 * Reusable Bottom Navigation Bar Component
 *
 * Provides consistent bottom navigation across the app.
 *
 * @param currentScreen The currently selected screen
 * @param onHomeClick Navigate to home
 * @param onSearchClick Navigate to search
 * @param onAlbumClick Navigate to album
 */
@Composable
fun AppBottomNav(
    currentScreen: BottomNavItem,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAlbumClick: () -> Unit
) {
    NavigationBar {
        // Home Tab
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = currentScreen == BottomNavItem.HOME,
            onClick = onHomeClick
        )

        // Search Tab
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            label = { Text("Search") },
            selected = currentScreen == BottomNavItem.SEARCH,
            onClick = onSearchClick
        )

        // Album Tab
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.PhotoAlbum,
                    contentDescription = "Album"
                )
            },
            label = { Text("Album") },
            selected = currentScreen == BottomNavItem.ALBUM,
            onClick = onAlbumClick
        )
    }
}
