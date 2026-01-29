package com.example.myapp.util

object ValidationUtil {

    /**
     * Validates username
     * Rules: 3-20 characters, alphanumeric and underscore only
     */
    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isBlank() ->
                ValidationResult(false, "Username cannot be empty")

            username.length < 3 ->
                ValidationResult(false, "Username must be at least 3 characters")

            username.length > 20 ->
                ValidationResult(false, "Username cannot exceed 20 characters")

            !username.matches(Regex("^[a-zA-Z0-9_]+$")) ->
                ValidationResult(false, "Username can only contain letters, numbers, and underscore")

            else ->
                ValidationResult(true)
        }
    }

    /**
     * Validates email (optional field)
     * Returns valid if blank (email is optional)
     */
    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(true)
        }
        return when {
            !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) ->
                ValidationResult(false, "Invalid email format")

            else ->
                ValidationResult(true)
        }
    }

    /**
     * Validates Bangladesh phone number
     * Format: 01[3-9]XXXXXXXX (11 digits total)
     * Valid operators: Grameenphone(017), Robi(018), Banglalink(019),
     *                  Teletalk(015), Airtel(016), Others(013,014)
     */
    fun validatePhoneNumber(phone: String): ValidationResult {
        return when {
            phone.isBlank() ->
                ValidationResult(false, "Phone number cannot be empty")

            phone.length != 11 ->
                ValidationResult(false, "Phone number must be 11 digits")

            !phone.matches(Regex("^01[3-9]\\d{8}$")) ->
                ValidationResult(false, "Invalid phone number. Must start with 013-019")

            else ->
                ValidationResult(true)
        }
    }

    /**
     * Validates password
     * Rules: 6-50 characters
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() ->
                ValidationResult(false, "Password cannot be empty")

            password.length < 6 ->
                ValidationResult(false, "Password must be at least 6 characters")

            password.length > 50 ->
                ValidationResult(false, "Password cannot exceed 50 characters")

            else ->
                ValidationResult(true)
        }
    }

    /**
     * Validates password confirmation
     * Checks if passwords match
     */
    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() ->
                ValidationResult(false, "Please confirm your password")

            password != confirmPassword ->
                ValidationResult(false, "Passwords do not match")

            else ->
                ValidationResult(true)
        }
    }

    /**
     * Validates OTP code
     * Rules: Exactly 6 digits
     */
    fun validateOtp(otp: String): ValidationResult {
        return when {
            otp.isBlank() ->
                ValidationResult(false, "OTP cannot be empty")

            otp.length != 6 ->
                ValidationResult(false, "OTP must be 6 digits")

            !otp.matches(Regex("^\\d{6}$")) ->
                ValidationResult(false, "OTP must contain only digits")

            else ->
                ValidationResult(true)
        }
    }

    /**
     * Formats phone number for display
     * Example: 01712345678 → 01712-345678
     */
    fun formatPhoneNumber(phone: String): String {
        return if (phone.length == 11 && phone.matches(Regex("^01[3-9]\\d{8}$"))) {
            "${phone.substring(0, 5)}-${phone.substring(5)}"
        } else {
            phone
        }
    }

    /**
     * Sanitizes phone number input
     * Removes spaces, dashes, and non-digit characters
     */
    fun sanitizePhoneNumber(phone: String): String {
        return phone.filter { it.isDigit() }
    }
}

/**
 * Result of validation operation
 * @property isValid Whether the validation passed
 * @property errorMessage Error message if validation failed, null if valid
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
