package com.example.socialmediavbsanalay.data.dataSource.user

import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    suspend fun addUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun getAllUsers(): List<User>
    fun searchUsers(query:String):Flow<List<User>>
    suspend fun getUsersByIds(userIds: List<String>): Map<String, User>
    suspend fun updateUserProfileImage(userId: String, imageUrl: String)
    suspend fun updateUserProfileImageByEmail(email: String, imageUrl: String)
    suspend fun updateBacgroundByEmail(email: String, imageUrl: String)
    suspend fun getUsersWithSharedStories(followingList: List<String>): List<User>
    suspend fun followUser(currentUserId: String, targetUserId: String): Boolean
    suspend fun unfollowUser(currentUserId: String, targetUserId: String): Boolean

    suspend fun isUserFollowing(currentUserId: String, targetUserId: String): Boolean
    suspend fun updateFollowerCount(userId: String, increment: Boolean)
    suspend fun getUserDocument(userId: String): DocumentSnapshot?
}
