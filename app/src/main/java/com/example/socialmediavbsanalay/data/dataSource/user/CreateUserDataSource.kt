package com.example.socialmediavbsanalay.data.dataSource.user

import com.example.socialmediavbsanalay.domain.model.User

interface CreateUserDataSource {
    suspend fun createUser(userId: String, user: User)
    suspend fun checkIfUserExists(email: String): Boolean
    suspend fun getUserById(userId: String): Result<User?>

    suspend fun fetchCurrentUser(): User?
    suspend fun getAllUsers(): Result<List<User>>


}