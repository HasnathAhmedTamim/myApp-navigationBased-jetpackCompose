package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.data.local.UserDatabase
import com.example.myapp.data.preferences.SessionManager
import com.example.myapp.data.repository.AuthRepository
import com.example.myapp.navigation.NavGraph
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.viewmodel.AuthViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel

/**
 * Main Activity - Application Entry Point
 *
 * Sets up the navigation graph and applies app theme.
 * Uses edge-to-edge display for modern Android UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Provide Room database, repository, and ViewModel
        val userDatabase = UserDatabase.getDatabase(applicationContext)
        val sessionManager = SessionManager(applicationContext)
        val authRepository = AuthRepository(userDatabase.userDao(), sessionManager)
        val authViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return AuthViewModel(authRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        val authViewModel = ViewModelProvider(this, authViewModelFactory)[AuthViewModel::class.java]

        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(authViewModel)
                }
            }
        }
    }
}

/**
 * Preview for entire app flow
 * Shows full system UI including status bar and navigation bar
 */
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "App Preview"
)
@Composable
fun AppPreview() {
    MyAppTheme {
        // This will not show real login logic in preview, but will render the UI
        // Pass a dummy ViewModel (not functional)
        // If you want a real preview, use dependency injection or a preview ViewModel
        // For now, just skip ViewModel logic in preview
        // NavGraph(authViewModel = ...) // Not possible without DI or a fake ViewModel
    }
}
