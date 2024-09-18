package com.example.socialmediavbsanalay.data.dataSource.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthDataSource {
    suspend fun signIn(email: String, password: String): FirebaseUser?
    suspend fun signUp(email: String, password: String): FirebaseUser?
    suspend fun getUserIdByEmail(email: String): String
    fun getCurrentUserEmail(): String
    fun getCurrentUserId():String?
}