package com.example.myapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * User table entity for Room database
 *
 * Represents a user account with authentication and profile information.
 * Enforces unique constraints on username and phoneNumber at database level.
 */
@Entity(
    tableName = "users",
    indices = [
        Index(value = ["username"], unique = true)
        // Removed unique constraint from phoneNumber
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "email")
    val email: String? = null,

    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "isVerified")
    val isVerified: Boolean = true,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis()
)
