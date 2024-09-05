package com.example.socialmediavbsanalay.domain.interactor.user

import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.database.Query
import javax.inject.Inject

class UserInteractor  @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun addUser(user: User) {
        userRepository.addUser(user)
    }

    suspend fun getUser(userId: String): User? {
        return userRepository.getUser(userId)
    }

    suspend fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }
}
