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
import com.example.myapp.viewmodel.AuthUiState
import com.example.myapp.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

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
    authViewModel: AuthViewModel,
    onLoginSuccess: (String, String) -> Unit
) {
    // ========================================
    // STATE
    // ========================================
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val uiState by authViewModel.uiState.collectAsState()
    val isLoading = uiState is AuthUiState.Loading
    val coroutineScope = rememberCoroutineScope()

    // React to login result
    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.LoginSuccess -> {
                onLoginSuccess(username, password)
                authViewModel.resetUiState()
            }
            is AuthUiState.Error -> {
                errorMessage = (uiState as AuthUiState.Error).message
            }
            else -> {}
        }
    }

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
                errorMessage = ""
            },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field with Toggle
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
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
                    password.length < 4 -> {
                        errorMessage = "Password must be at least 4 characters"
                    }
                    else -> {
                        errorMessage = ""
                        coroutineScope.launch {
                            authViewModel.login(username, password)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
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
