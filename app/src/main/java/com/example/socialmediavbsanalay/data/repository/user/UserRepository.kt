package com.example.socialmediavbsanalay.data.repository.user

import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.database.Query
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun getAllUsers(): List<User>
    fun searchUsers(query: String): Flow<List<User>>
    suspend fun getUsersByIds(userIds: List<String>): Map<String, User>
    suspend fun updateUserProfileImage(userId: String, imageUrl: String): Result<Unit>
    suspend fun updateUserProfileImageByEmail(email: String, imageUrl: String)

}

