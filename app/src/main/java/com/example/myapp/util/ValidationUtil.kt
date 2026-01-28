package com.example.myapp.util

object ValidationUtil {

    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult(false, "Username cannot be empty")
            username.length < 3 -> ValidationResult(false, "Username must be at least 3 characters")
            username.length > 20 -> ValidationResult(false, "Username must be less than 20 characters")
            !username.matches(Regex("^[a-zA-Z0-9_]+$")) ->
                ValidationResult(false, "Username can only contain letters, numbers, and underscore")
            else -> ValidationResult(true)
        }
    }

    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(true)
        }
        return when {
            !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) ->
                ValidationResult(false, "Invalid email format")
            else -> ValidationResult(true)
        }
    }

    fun validatePhoneNumber(phone: String): ValidationResult {
        return when {
            phone.isBlank() -> ValidationResult(false, "Phone number cannot be empty")
            phone.length != 11 -> ValidationResult(false, "Phone number must be 11 digits")
            !phone.startsWith("01") -> ValidationResult(false, "Phone number must start with 01")
            !phone.matches(Regex("^\\d+$")) -> ValidationResult(false, "Phone number must contain only digits")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "Password cannot be empty")
            password.length < 6 -> ValidationResult(false, "Password must be at least 6 characters")
            password.length > 50 -> ValidationResult(false, "Password must be less than 50 characters")
            else -> ValidationResult(true)
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult(false, "Please confirm your password")
            password != confirmPassword -> ValidationResult(false, "Passwords do not match")
            else -> ValidationResult(true)
        }
    }

    fun validateOtp(otp: String): ValidationResult {
        return when {
            otp.isBlank() -> ValidationResult(false, "OTP cannot be empty")
            otp.length != 6 -> ValidationResult(false, "OTP must be 6 digits")
            !otp.matches(Regex("^\\d+$")) -> ValidationResult(false, "OTP must contain only digits")
            else -> ValidationResult(true)
        }
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
