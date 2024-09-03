package com.example.hevintechnowebpractical.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hevintechnowebpractical.data.local.UserEntity
import com.example.hevintechnowebpractical.data.model.User
import com.example.hevintechnowebpractical.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private var currentPage = 0
    private val pageSize = 10

    init {
        viewModelScope.launch {
            val localUsers = repository.getLocalUsers()
            _users.value = localUsers.map { it.toUser() }
            fetchUsers()
        }
    }

    fun fetchUsers() {
        viewModelScope.launch {
            val response = repository.fetchUsers(pageSize, currentPage * pageSize)
            val usersFromApi = response.users.map { it.toEntity() }
            repository.saveUsers(usersFromApi)
            _users.value = _users.value.orEmpty() + response.users
            currentPage++
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            val usersFromDb = repository.searchUsers(query)
            _users.value = usersFromDb.map { it.toUser() }
        }
    }

    fun sortUsersByFirstName() {
        viewModelScope.launch {
            val usersFromDb = repository.getUsersSortedByFirstName()
            _users.value = usersFromDb.map { it.toUser() }
        }
    }

    fun sortUsersByEmail() {
        viewModelScope.launch {
            val usersFromDb = repository.getUsersSortedByEmail()
            _users.value = usersFromDb.map { it.toUser() }
        }
    }
}

fun User.toEntity() = UserEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    imageUrl = image,
    email = email
)

fun UserEntity.toUser() = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    image = imageUrl,
    email = email
)
