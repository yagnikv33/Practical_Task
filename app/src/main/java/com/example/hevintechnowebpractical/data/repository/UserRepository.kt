package com.example.hevintechnowebpractical.data.repository

import com.example.hevintechnowebpractical.data.local.UserDao
import com.example.hevintechnowebpractical.data.local.UserEntity
import com.example.hevintechnowebpractical.data.model.UserResponse
import com.example.hevintechnowebpractical.data.remote.UserService

class UserRepository(
    private val userService: UserService,
    private val userDao: UserDao
) {

    suspend fun fetchUsers(limit: Int, skip: Int): UserResponse {
        return userService.getUsers(limit, skip)
    }

    suspend fun getLocalUsers(): List<UserEntity> {
        return userDao.getAllUsers()
    }

    suspend fun saveUsers(users: List<UserEntity>) {
        userDao.insertUsers(users)
    }


    suspend fun searchUsers(query: String): List<UserEntity> {
        return userDao.searchUsers(query)
    }

    suspend fun getUsersSortedByFirstName(): List<UserEntity> {
        return userDao.getUsersSortedByFirstName()
    }

    suspend fun getUsersSortedByEmail(): List<UserEntity> {
        return userDao.getUsersSortedByEmail()
    }
}
