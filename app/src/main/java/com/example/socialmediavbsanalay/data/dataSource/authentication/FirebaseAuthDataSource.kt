package com.example.socialmediavbsanalay.data.dataSource.authentication

import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FirebaseAuthDataSource {
    suspend fun signIn(email: String, password: String): FirebaseUser?
    suspend fun signUp(email: String, password: String): FirebaseUser?
    suspend fun getUserIdByEmail(email: String): String
    suspend fun getUserByEmail(email: String): User?
    fun getCurrentUserEmail(): String
    fun getCurrentUserId():String?
    fun fetchUserIdByAuthId(authId: String): Flow<String?> = flow{}
    fun signOut()
    suspend fun getUserProfileImageByEmail(email: String):String?
    suspend fun getBacgroundByEmail(email: String):String?
}
