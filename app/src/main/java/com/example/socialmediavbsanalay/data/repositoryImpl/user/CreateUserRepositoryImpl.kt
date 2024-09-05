package com.example.socialmediavbsanalay.data.repositoryImpl.user

import com.example.socialmediavbsanalay.data.dataSource.user.CreateUserDataSource
import com.example.socialmediavbsanalay.data.repository.user.CreateUserRepository
import com.example.socialmediavbsanalay.domain.model.User

class CreateUserRepositoryImpl(private val createUserDataSource: CreateUserDataSource) : CreateUserRepository {
    override suspend fun createUser(userId: String, user: User): Result<Unit> {
        return try {
            createUserDataSource.createUser(userId, user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}