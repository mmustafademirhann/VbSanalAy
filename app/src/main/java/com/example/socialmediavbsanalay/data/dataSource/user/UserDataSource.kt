package com.example.socialmediavbsanalay.data.dataSource.user

import com.example.socialmediavbsanalay.domain.model.User

interface UserDataSource {
    suspend fun addUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun getAllUsers(): List<User>
}
