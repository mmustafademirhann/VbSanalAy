package com.example.socialmediavbsanalay.domain.interactor.user

import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.google.firebase.database.Query
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val userRepository: UserRepository
) {
    fun searchUsersByName(name: String): Query {
        // Ensure the method exists in UserRepository
        return userRepository.searchUsersByName(name)
    }
}
