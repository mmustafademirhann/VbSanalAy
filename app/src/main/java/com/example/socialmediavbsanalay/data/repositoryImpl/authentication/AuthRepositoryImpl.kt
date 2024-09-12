package com.example.socialmediavbsanalay.data.repositoryImpl.authentication

import com.example.socialmediavbsanalay.data.dataSource.authentication.FirebaseAuthDataSource
import com.example.socialmediavbsanalay.data.repository.authentication.AuthRepository
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuthDataSource.signIn(email, password)
            Result.success(result)
        } catch (e: FirebaseAuthInvalidUserException) {
            // This exception indicates the user does not exist
            Result.failure(Exception("User not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val authResult = firebaseAuthDataSource.signUp(email, password)

            Result.success(authResult)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}