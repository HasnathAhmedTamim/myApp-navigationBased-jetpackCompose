package com.example.myapp.data.repository

import com.example.myapp.data.local.UserDao
import com.example.myapp.data.local.UserEntity
import com.example.myapp.data.preferences.SessionManager
import kotlinx.coroutines.flow.Flow

// This file was previously misplaced. The AuthRepository class should be here, not in viewmodel/AuthViewModel.kt.
// If you see duplicate class errors, remove the AuthRepository class from viewmodel/AuthViewModel.kt.

class AuthRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {

    suspend fun registerUser(user: UserEntity): Result<Long> {
        return try {
            val userId = userDao.insertUser(user)
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        return userDao.isUsernameExists(username) == 0
    }

    suspend fun loginUser(username: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.verifyLogin(username, password)
            if (user != null) {
                sessionManager.saveLoginSession(user.id, user.username)
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials or account not verified"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }

    suspend fun getUserById(userId: Long): UserEntity? {
        return userDao.getUserById(userId)
    }

    suspend fun getUserByPhoneNumber(phoneNumber: String): UserEntity? {
        return userDao.getUserByPhoneNumber(phoneNumber)
    }

    suspend fun markUserAsVerified(phoneNumber: String): Int {
        return userDao.markUserAsVerified(phoneNumber)
    }

    suspend fun updatePassword(phoneNumber: String, newPassword: String): Int {
        return userDao.updatePasswordByPhone(phoneNumber, newPassword)
    }

    suspend fun getUserByUsernameAndPhone(username: String, phoneNumber: String): UserEntity? {
        return userDao.getUserByUsernameAndPhone(username, phoneNumber)
    }

    val isLoggedIn: Flow<Boolean> = sessionManager.isLoggedIn
    val currentUserId: Flow<Long?> = sessionManager.userId
    val currentUsername: Flow<String?> = sessionManager.username
}
