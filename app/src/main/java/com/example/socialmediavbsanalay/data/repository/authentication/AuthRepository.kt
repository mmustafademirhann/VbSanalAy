package com.example.socialmediavbsanalay.data.repository.authentication

import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<FirebaseUser?>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser?>
    suspend fun getUserIdByEmail(email: String):String

    suspend fun getUserByEmail(email: String): Result<User?>
    fun getCurrentUserEmail(): String
    fun fetchUserIdByAuthId(authId: String): Flow<String?> = flow{}
    fun getCurrentUserId():String?
    fun signOut()
    suspend fun getUserProfileImageByEmail(email: String): Result<String?>
    suspend fun getBacgroundByEmail(email: String): Result<String?>

}
