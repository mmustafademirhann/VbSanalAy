package com.example.socialmediavbsanalay.data.repositoryImpl.user

import com.example.socialmediavbsanalay.data.dataSource.user.UserDataSource
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {

    override suspend fun addUser(user: User) {
        userDataSource.addUser(user)
    }

    override suspend fun getUser(userId: String): User? {
        return userDataSource.getUser(userId)
    }

    override suspend fun getAllUsers(): List<User> {
        return userDataSource.getAllUsers()
    }
    override fun searchUsers(query: String): Flow<List<User>> {
        return userDataSource.searchUsers(query)
    }
    override suspend fun getUsersByIds(userIds: List<String>): Map<String, User> {
        return userDataSource.getUsersByIds(userIds)
    }
    override suspend fun updateUserProfileImage(userId: String, imageUrl: String): Result<Unit> {
        return try {
            userDataSource.updateUserProfileImage(userId, imageUrl) // Call the data source method
            Result.success(Unit) // Return success result
        } catch (e: Exception) {
            // Handle the exception and return failure result
            Result.failure(Exception("Failed to update user profile image in repository: ${e.message}", e))
        }
    }

    override suspend fun updateUserProfileImageByEmail(email: String, imageUrl: String) {
        userDataSource.updateUserProfileImageByEmail(email, imageUrl)
    }

    override suspend fun updateBacgroundByEmail(email: String, imageUrl: String) {
        userDataSource.updateBacgroundByEmail(email, imageUrl)
    }
    override suspend fun fetchUsersWithSharedStories(followingList: List<String>): List<User> {
        return userDataSource.getUsersWithSharedStories(followingList)
    }
    override suspend fun followUser(currentUserId: String, targetUserId: String): Boolean {
        return userDataSource.followUser(currentUserId, targetUserId)
    }

    override suspend fun unfollowUser(currentUserId: String, targetUserId: String): Boolean {
        return userDataSource.unfollowUser(currentUserId, targetUserId)
    }
    override suspend fun isUserFollowing(currentUserId: String, targetUserId: String): Boolean {
        return userDataSource.isUserFollowing(currentUserId, targetUserId)
    }
    override suspend fun updateFollowerCount(targetUserId: String, increment: Boolean) {
        return userDataSource.updateFollowerCount(targetUserId, increment)
    }
    override suspend fun getUserDocument(userId: String): DocumentSnapshot? {
        return userDataSource.getUserDocument(userId)
    }

}
