package com.example.socialmediavbsanalay.data.repositoryImpl.authentication

import android.util.Log
import com.example.socialmediavbsanalay.data.dataSource.authentication.FirebaseAuthDataSource
import com.example.socialmediavbsanalay.data.dataSource.user.CreateUserDataSource
import com.example.socialmediavbsanalay.data.repository.authentication.AuthRepository
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val createUserDataSource: CreateUserDataSource
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

    override suspend fun getUserIdByEmail(email: String): String {
        return try {
            firebaseAuthDataSource.getUserIdByEmail(email) // Flow'dan ilk değeri al
        } catch (e: FirebaseAuthInvalidUserException) {
            throw Exception("User not found with email: $email", e)
        } catch (e: Exception) {
            throw Exception("Failed to retrieve userId: ${e.message}", e)
        }
    }

    override fun getCurrentUserEmail(): String {
        return firebaseAuthDataSource.getCurrentUserEmail()
    }
    override fun getCurrentUserId():String?{
        val user= FirebaseAuth.getInstance().currentUser
        return user?.uid
    }
    override fun fetchUserIdByAuthId(authId: String): Flow<String?> = flow {
         firebaseAuthDataSource.fetchUserIdByAuthId(authId)
    }
    override fun signOut() {
        firebaseAuthDataSource.signOut()
    }
    override suspend fun getUserProfileImageByEmail(email: String): Result<String?> {
        return try {
            val imageUrl = firebaseAuthDataSource.getUserProfileImageByEmail(email)
            Result.success(imageUrl) // Return success with the image URL
        } catch (exception: Exception) {
            Log.e("UserRepository", "Error getting user profile image: ${exception.message}")
            Result.failure(exception) // Return failure with the exception
        }
    }

}