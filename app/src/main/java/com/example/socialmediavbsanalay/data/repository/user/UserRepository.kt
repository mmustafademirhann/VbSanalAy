package com.example.socialmediavbsanalay.data.repository.user

import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.database.Query

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun getAllUsers(): List<User>
}
