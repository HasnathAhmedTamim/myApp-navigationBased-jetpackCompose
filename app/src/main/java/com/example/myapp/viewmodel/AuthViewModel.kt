package com.example.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.local.UserEntity
import com.example.myapp.data.repository.AuthRepository
import com.example.myapp.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    object LoginSuccess : AuthUiState()
    object SignupSuccess : AuthUiState()
    object PasswordResetSuccess : AuthUiState()
    data class PhoneNumberFound(val phoneNumber: String) : AuthUiState()
    object OtpVerified : AuthUiState()
    object LogoutSuccess : AuthUiState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // ✅ Better: Use asStateFlow()
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Expose isLoggedIn and currentUsername as StateFlow for Compose
    val isLoggedIn = repository.isLoggedIn.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    val currentUsername = repository.currentUsername.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        null
    )

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.update { AuthUiState.Loading }  // ✅ Better: Use update
            val result = repository.loginUser(username, password)
            _uiState.update {
                if (result.isSuccess) AuthUiState.LoginSuccess
                else AuthUiState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun signup(user: UserEntity) {
        viewModelScope.launch {
            _uiState.update { AuthUiState.Loading }
            // Only check for unique username before registering
            val usernameAvailable = repository.isUsernameAvailable(user.username)
            if (!usernameAvailable) {
                _uiState.update { AuthUiState.Error("Username already registered") }
                return@launch
            }
            val result = repository.registerUser(user)
            _uiState.update {
                if (result.isSuccess) AuthUiState.SignupSuccess
                else AuthUiState.Error(result.exceptionOrNull()?.message ?: "Signup failed")
            }
        }
    }

    fun resetUiState() {
        _uiState.update { AuthUiState.Idle }
    }

    fun checkPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _uiState.update { AuthUiState.Loading }
            try {
                val user = repository.getUserByPhoneNumber(phoneNumber)
                _uiState.update {
                    if (user != null) {
                        AuthUiState.PhoneNumberFound(phoneNumber)
                    } else {
                        AuthUiState.Error("No account found with this phone number")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    AuthUiState.Error(e.message ?: "Error verifying phone number")
                }
            }
        }
    }

    fun verifyOtp(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _uiState.update { AuthUiState.Loading }
            if (otp == com.example.myapp.util.Constants.VALID_OTP) {
                repository.markUserAsVerified(phoneNumber)
                _uiState.update { AuthUiState.OtpVerified }
            } else {
                _uiState.update {
                    AuthUiState.Error("Invalid OTP. Please enter ${com.example.myapp.util.Constants.VALID_OTP}")
                }
            }
        }
    }

    fun resetPassword(username: String, phoneNumber: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.update { AuthUiState.Loading }

            val passwordValidation = com.example.myapp.util.ValidationUtil.validatePassword(newPassword)
            val confirmValidation = com.example.myapp.util.ValidationUtil.validateConfirmPassword(newPassword, confirmPassword)

            if (!passwordValidation.isValid) {
                _uiState.update {
                    AuthUiState.Error(passwordValidation.errorMessage ?: "Invalid password")
                }
                return@launch
            }
            if (!confirmValidation.isValid) {
                _uiState.update {
                    AuthUiState.Error(confirmValidation.errorMessage ?: "Passwords do not match")
                }
                return@launch
            }

            try {
                val user = repository.getUserByUsernameAndPhone(username, phoneNumber)
                if (user == null) {
                    _uiState.update {
                        AuthUiState.Error("No account found with this username and phone number")
                    }
                    return@launch
                }
                val rows = repository.updatePassword(username, phoneNumber, newPassword)
                _uiState.update {
                    if (rows > 0) {
                        AuthUiState.PasswordResetSuccess
                    } else {
                        AuthUiState.Error("Failed to reset password. Try again.")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    AuthUiState.Error(e.message ?: "Error resetting password")
                }
            }
        }
    }

    // ✅ Better: Use first() instead of stateIn
    suspend fun getCurrentUser(): UserEntity? {
        val userId = repository.currentUserId.first()
        return userId?.let { repository.getUserById(it) }
    }

    // Expose a public suspend function to get the current user as a model.User for use in NavGraph
    suspend fun getCurrentUserModel(): User? {
        val userId = repository.currentUserId.first()
        return userId?.let {
            val entity = repository.getUserById(it)
            entity?.let { e ->
                User(
                    username = e.username,
                    email = e.email ?: "",
                    phone = e.phoneNumber
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.update { AuthUiState.LogoutSuccess }
        }
    }

    fun deleteCurrentUser(onDeleted: () -> Unit) {
        viewModelScope.launch {
            val username = repository.currentUsername.first()
            if (username != null) {
                repository.deleteUserByUsername(username)
                logout()
                onDeleted()
            }
        }
    }
}
