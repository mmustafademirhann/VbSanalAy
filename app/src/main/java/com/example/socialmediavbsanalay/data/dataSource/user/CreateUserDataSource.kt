package com.example.socialmediavbsanalay.data.dataSource.user

import com.example.socialmediavbsanalay.domain.model.User

interface CreateUserDataSource {
    suspend fun createUser(userId: String, user: User)
    suspend fun checkIfUserExists(email: String): Boolean
}