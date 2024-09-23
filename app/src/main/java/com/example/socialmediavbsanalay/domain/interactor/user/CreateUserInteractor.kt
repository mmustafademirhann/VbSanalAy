package com.example.socialmediavbsanalay.domain.interactor.user

import com.example.socialmediavbsanalay.data.repository.user.CreateUserRepository
import com.example.socialmediavbsanalay.domain.model.User

class CreateUserInteractor(private val repository: CreateUserRepository) {
    suspend fun createUser(userId: String, user: User): Result<Unit> {
        return repository.createUser(userId, user)
    }
    suspend fun checkIfUserExists(email: String): Boolean {
        return repository.checkIfUserExists(email)
    }
    suspend fun getUserById(userId: String): Result<User?>{
        return repository.getUserById(userId)
    }
}