package com.example.socialmediavbsanalay.data.repository.user

import com.example.socialmediavbsanalay.domain.model.User

interface CreateUserRepository {
    suspend fun createUser(userId: String, user: User): Result<Unit>
    suspend fun checkIfUserExists(email: String): Boolean
    suspend fun getUserById(userId: String): Result<User?>
}