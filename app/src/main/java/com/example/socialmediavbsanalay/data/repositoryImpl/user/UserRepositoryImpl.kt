package com.example.socialmediavbsanalay.data.repositoryImpl.user

import com.example.socialmediavbsanalay.data.dataSource.user.UserDataSource
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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
}
