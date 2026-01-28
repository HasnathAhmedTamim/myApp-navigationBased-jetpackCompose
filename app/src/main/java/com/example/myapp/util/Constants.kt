package com.example.myapp.util

object Constants {
    const val VALID_OTP = "111111"
    const val OTP_LENGTH = 6

    const val MIN_USERNAME_LENGTH = 3
    const val MAX_USERNAME_LENGTH = 20
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_PASSWORD_LENGTH = 50
    const val PHONE_NUMBER_LENGTH = 11

    const val OTP_SENT_MESSAGE = "OTP has been sent to your phone"
    const val OTP_VERIFIED_MESSAGE = "OTP verified successfully!"
    const val ACCOUNT_CREATED_MESSAGE = "Account created successfully!"
    const val LOGIN_SUCCESS_MESSAGE = "Welcome back!"
    const val PASSWORD_RESET_MESSAGE = "Password reset successfully!"
    const val LOGOUT_MESSAGE = "Logged out successfully"

    const val INVALID_OTP_MESSAGE = "Invalid OTP. Please enter 111111"
    const val PHONE_NOT_FOUND_MESSAGE = "No account found with this phone number"
    const val PHONE_NOT_VERIFIED_MESSAGE = "Account not verified"
    const val USERNAME_EXISTS_MESSAGE = "Username already taken"
    const val PHONE_EXISTS_MESSAGE = "Phone number already registered"
    const val NETWORK_ERROR_MESSAGE = "Something went wrong. Please try again."
    const val INVALID_CREDENTIALS_MESSAGE = "Invalid username or password"
}
