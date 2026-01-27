package com.example.myapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Login Screen
 *
 * Entry point of the application.
 * Allows user to login with username and password.
 *
 * @param onLoginSuccess Callback when login is successful (username, password)
 */
@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit
) {
    // ========================================
    // STATE
    // ========================================
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }  // ✅ Toggle visibility
    var isLoading by remember { mutableStateOf(false) }  // ✅ Loading state
    var errorMessage by remember { mutableStateOf("") }  // ✅ Error handling

    // ========================================
    // UI
    // ========================================
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Login to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Username Field
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                errorMessage = ""  // Clear error on input
            },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,  // ✅ Single line input
            isError = errorMessage.isNotEmpty()  // ✅ Show error state
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field with Toggle
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""  // Clear error on input
            },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {  // ✅ Eye icon to toggle
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage.isNotEmpty()
        )

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = {
                when {
                    username.isEmpty() -> {
                        errorMessage = "Please enter username"
                    }
                    password.isEmpty() -> {
                        errorMessage = "Please enter password"
                    }
                    password.length < 4 -> {  // ✅ Minimum length
                        errorMessage = "Password must be at least 4 characters"
                    }
                    else -> {
                        isLoading = true
                        onLoginSuccess(username, password)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading  // ✅ Disable during loading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Login")
            }
        }
    }
}
