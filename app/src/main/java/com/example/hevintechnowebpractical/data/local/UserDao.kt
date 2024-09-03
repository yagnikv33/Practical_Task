package com.example.hevintechnowebpractical.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Insert
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("SELECT * FROM users ORDER BY firstName ASC")
    suspend fun getUsersSortedByFirstName(): List<UserEntity>

    @Query("SELECT * FROM users ORDER BY email ASC")
    suspend fun getUsersSortedByEmail(): List<UserEntity>

    @Query("SELECT * FROM users WHERE firstName LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%'")
    suspend fun searchUsers(query: String): List<UserEntity>
}