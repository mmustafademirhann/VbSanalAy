package com.example.socialmediavbsanalay.data.dataSource.authentication

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthDataSource {
    suspend fun signIn(email: String, password: String): FirebaseUser?
    suspend fun signUp(email: String, password: String): FirebaseUser?
}