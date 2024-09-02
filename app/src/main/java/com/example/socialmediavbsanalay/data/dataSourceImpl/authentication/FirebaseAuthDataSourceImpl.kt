package com.example.socialmediavbsanalay.data.dataSourceImpl.authentication

import com.example.socialmediavbsanalay.data.dataSource.authentication.FirebaseAuthDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    //Hilt ile provide edebilmek için @Inject ifadesini kullandık.
    //Inject görülen yerde Hilt kütüphanesi provide edeceği nesneyi arar. Inject yoksa aramaz.
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthDataSource {

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await().user
    }
    //selam yorum

    override suspend fun signUp(email: String, password: String): FirebaseUser? {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
    }
}