package com.example.myapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long  // ✅ Keep as is - Room handles this

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?  // ✅ Already correct

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?  // ✅ Already correct

    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber")
    suspend fun getUserByPhoneNumber(phoneNumber: String): UserEntity?  // ✅ Already correct

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun verifyLogin(username: String, password: String): UserEntity?  // Allow login regardless of isVerified

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>  // ✅ Already correct

    @Query("UPDATE users SET isVerified = 1 WHERE phoneNumber = :phoneNumber")
    suspend fun markUserAsVerified(phoneNumber: String): Int  // ✅ Keep as is

    @Query("UPDATE users SET password = :newPassword WHERE phoneNumber = :phoneNumber")
    suspend fun updatePasswordByPhone(phoneNumber: String, newPassword: String): Int  // ✅ Keep as is

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun isUsernameExists(username: String): Int  // ✅ Keep as is

    @Query("SELECT * FROM users WHERE username = :username AND phoneNumber = :phoneNumber")
    suspend fun getUserByUsernameAndPhone(username: String, phoneNumber: String): UserEntity?
}
