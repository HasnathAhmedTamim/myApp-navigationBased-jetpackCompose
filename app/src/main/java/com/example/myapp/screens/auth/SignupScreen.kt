package com.example.myapp.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.components.AuthButton
import com.example.myapp.components.AuthTextField
import com.example.myapp.components.LoadingDialog
import com.example.myapp.viewmodel.AuthUiState
import com.example.myapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onSignupSuccess: (String) -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.uiState.collectAsState()

    // Handle UI State
    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.SignupSuccess -> {
                viewModel.resetUiState()
                onSignupSuccess(phoneNumber)
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Create your account",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Username Field
                AuthTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Username",
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field (Optional)
                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email (Optional)",
                    keyboardType = KeyboardType.Email,
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number Field
                AuthTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = "Phone Number",
                    keyboardType = KeyboardType.Phone,
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Field
                AuthTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Error Message
                if (uiState is AuthUiState.Error) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = (uiState as AuthUiState.Error).message,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Signup Button
                AuthButton(
                    text = "Sign Up",
                    onClick = {
                        // Validation before signup
                        val usernameResult = com.example.myapp.util.ValidationUtil.validateUsername(username)
                        if (!usernameResult.isValid) {
                            validationError = usernameResult.errorMessage
                            return@AuthButton
                        }
                        val emailResult = com.example.myapp.util.ValidationUtil.validateEmail(email)
                        if (!emailResult.isValid) {
                            validationError = emailResult.errorMessage
                            return@AuthButton
                        }
                        val phoneResult = com.example.myapp.util.ValidationUtil.validatePhoneNumber(phoneNumber)
                        if (!phoneResult.isValid) {
                            validationError = phoneResult.errorMessage
                            return@AuthButton
                        }
                        val passwordResult = com.example.myapp.util.ValidationUtil.validatePassword(password)
                        if (!passwordResult.isValid) {
                            validationError = passwordResult.errorMessage
                            return@AuthButton
                        }
                        val confirmResult = com.example.myapp.util.ValidationUtil.validateConfirmPassword(password, confirmPassword)
                        if (!confirmResult.isValid) {
                            validationError = confirmResult.errorMessage
                            return@AuthButton
                        }
                        validationError = null
                        val user = com.example.myapp.data.local.UserEntity(
                            username = username,
                            email = if (email.isBlank()) null else email,
                            phoneNumber = phoneNumber,
                            password = password
                        )
                        viewModel.signup(user)
                    },
                    enabled = uiState !is AuthUiState.Loading
                )

                if (validationError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = validationError ?: "",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account?")
                    TextButton(onClick = onLoginClick) {
                        Text("Login")
                    }
                }
            }

            // Loading Dialog
            if (uiState is AuthUiState.Loading) {
                LoadingDialog("Creating account...")
            }
        }
    }
}
