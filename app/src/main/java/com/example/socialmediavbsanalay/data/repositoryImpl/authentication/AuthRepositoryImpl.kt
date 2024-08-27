package com.example.socialmediavbsanalay.data.repositoryImpl.authentication

import com.example.socialmediavbsanalay.data.dataSource.authentication.FirebaseAuthDataSource
import com.example.socialmediavbsanalay.data.repository.authentication.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<FirebaseUser?> {
        return firebaseAuthDataSource.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): Result<FirebaseUser?> {
        return firebaseAuthDataSource.signUp(email, password)
    }
}