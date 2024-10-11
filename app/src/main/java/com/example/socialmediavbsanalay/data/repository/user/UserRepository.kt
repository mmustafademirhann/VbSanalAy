package com.example.socialmediavbsanalay.data.repository.user

import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun getAllUsers(): List<User>
    fun searchUsers(query: String): Flow<List<User>>
    suspend fun getUsersByIds(userIds: List<String>): Map<String, User>
    suspend fun updateUserProfileImage(userId: String, imageUrl: String): Result<Unit>
    suspend fun updateUserProfileImageByEmail(email: String, imageUrl: String)
    suspend fun updateBacgroundByEmail(email: String, imageUrl: String)
    suspend fun fetchUsersWithSharedStories(): List<User>
    suspend fun followUser(currentUserId: String, targetUserId: String): Boolean
    suspend fun unfollowUser(currentUserId: String, targetUserId: String): Boolean
    suspend fun isUserFollowing(currentUserId: String, targetUserId: String): Boolean
    suspend fun updateFollowerCount(targetUserId: String, increment: Boolean)
    suspend fun getUserDocument(userId: String): DocumentSnapshot?
}

