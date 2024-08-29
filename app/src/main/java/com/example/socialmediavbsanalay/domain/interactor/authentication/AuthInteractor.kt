package com.example.socialmediavbsanalay.domain.interactor.authentication

import com.example.socialmediavbsanalay.data.repository.authentication.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun signIn(email: String, password: String): Result<FirebaseUser?> {
        return authRepository.signIn(email, password)
    }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser?> {
        return authRepository.signUp(email, password)
    }

}