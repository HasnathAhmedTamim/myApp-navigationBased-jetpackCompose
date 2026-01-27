package com.example.myapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.components.AppBottomNav
import com.example.myapp.components.AppTopBar
import com.example.myapp.components.BottomNavItem

/**
 * Search Screen
 *
 * Features same TopBar and BottomNav as HomeScreen.
 * User can search content here.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    username: String,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAlbumClick: () -> Unit,
    onCardClick: (String, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

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
                currentScreen = BottomNavItem.SEARCH,  // ✅ Fixed: SEARCH selected
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Search Screen Content")
        }
    }
}
