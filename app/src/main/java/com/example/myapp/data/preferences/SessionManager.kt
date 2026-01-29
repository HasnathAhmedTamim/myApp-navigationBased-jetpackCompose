package com.example.myapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

/**
 * Session Manager - Handles user session persistence
 *
 * Uses DataStore for storing login state, user info, and preferences.
 * All operations are thread-safe and coroutine-based.
 */
class SessionManager(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = longPreferencesKey("user_id")
        private val USERNAME = stringPreferencesKey("username")
        private val PHONE_NUMBER = stringPreferencesKey("phone_number")
        private val LAST_LOGIN = longPreferencesKey("last_login")
    }

    /**
     * Session data holder
     */
    data class SessionData(
        val isLoggedIn: Boolean = false,
        val userId: Long? = null,
        val username: String? = null,
        val phoneNumber: String? = null,
        val lastLogin: Long? = null
    )

    // ========== WRITE OPERATIONS ===========

    /**
     * Saves user session after successful login
     */
    suspend fun saveLoginSession(userId: Long, username: String, phoneNumber: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = userId
            preferences[USERNAME] = username
            preferences[PHONE_NUMBER] = phoneNumber
            preferences[LAST_LOGIN] = System.currentTimeMillis()
        }
    }

    /**
     * Clears all session data (logout)
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    /**
     * Updates username only
     */
    suspend fun updateUsername(newUsername: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = newUsername
        }
    }

    // ========== READ OPERATIONS (Flow) ==========

    /**
     * Complete session data as reactive Flow
     */
    val sessionData: Flow<SessionData> = context.dataStore.data
        .map { preferences ->
            SessionData(
                isLoggedIn = preferences[IS_LOGGED_IN] ?: false,
                userId = preferences[USER_ID],
                username = preferences[USERNAME],
                phoneNumber = preferences[PHONE_NUMBER],
                lastLogin = preferences[LAST_LOGIN]
            )
        }

    /**
     * Login status as reactive Flow
     */
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    /**
     * User ID as reactive Flow
     */
    val userId: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID]
        }

    /**
     * Username as reactive Flow
     */
    val username: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME]
        }

    /**
     * Phone number as reactive Flow
     */
    val phoneNumber: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PHONE_NUMBER]
        }

    // ========== SYNCHRONOUS HELPERS ==========

    /**
     * Get current session data (blocking)
     * Use when you need immediate value
     */
    suspend fun getCurrentSession(): SessionData {
        return sessionData.first()
    }

    /**
     * Check if user is logged in (blocking)
     */
    suspend fun checkLoginStatus(): Boolean {
        return context.dataStore.data.first()[IS_LOGGED_IN] ?: false
    }

    /**
     * Get user ID (blocking)
     */
    suspend fun getUserId(): Long? {
        return context.dataStore.data.first()[USER_ID]
    }

    /**
     * Get username (blocking)
     */
    suspend fun getUsername(): String? {
        return context.dataStore.data.first()[USERNAME]
    }
}
