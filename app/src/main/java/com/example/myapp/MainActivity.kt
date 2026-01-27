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
import com.example.myapp.navigation.NavGraph
import com.example.myapp.ui.theme.MyAppTheme

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
        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
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
        NavGraph()
    }
}
