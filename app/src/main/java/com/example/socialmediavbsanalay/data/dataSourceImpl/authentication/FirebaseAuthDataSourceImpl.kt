package com.example.socialmediavbsanalay.data.dataSourceImpl.authentication

import com.example.socialmediavbsanalay.data.dataSource.authentication.FirebaseAuthDataSource
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    //Hilt ile provide edebilmek için @Inject ifadesini kullandık.
    //Inject görülen yerde Hilt kütüphanesi provide edeceği nesneyi arar. Inject yoksa aramaz.
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FirebaseAuthDataSource {

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await().user
    }

    override suspend fun signUp(email: String, password: String): FirebaseUser? {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
    }
    override suspend fun getUserIdByEmail(email: String): String {
        return withContext(Dispatchers.IO) {
            val querySnapshot = firestore.collection("user")
                .whereEqualTo("email", email)
                .get()
                .await()

            val userDocument = querySnapshot.documents.firstOrNull()

            // If user not found, throw an exception
            if (userDocument != null) {
                userDocument.id
            } else {
                throw FirebaseAuthInvalidUserException(
                    "ERROR_USER_NOT_FOUND",
                    "User not found with email: $email"
                )
            }
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return withContext(Dispatchers.IO) {
            val querySnapshot = firestore.collection("user")
                .whereEqualTo("email", email)
                .get()
                .await()

            val userDocument = querySnapshot.documents.firstOrNull()

            // If user not found, throw an exception
            if (userDocument != null) {
                userDocument.toObject(User::class.java)
            } else {
                throw FirebaseAuthInvalidUserException(
                    "ERROR_USER_NOT_FOUND",
                    "User not found with email: $email"
                )
            }
        }
    }
    override fun getCurrentUserEmail(): String {
        return firebaseAuth.currentUser?.email.toString()
    }
    override fun getCurrentUserId():String?{
        val user=FirebaseAuth.getInstance().currentUser
        return user?.uid
    }
    override fun fetchUserIdByAuthId(authId: String): Flow<String?> = flow {
        val userDocument = firestore.collection("user").document(authId).get().await()

        if (userDocument.exists()) {
            val userId = userDocument.getString("id") // Firestore'daki 'id' alanını al
            emit(userId) // Kullanıcı ID'sini emit et
        } else {
            emit(null) // Belge yoksa null döndür
        }
    }
    override fun signOut() {
        firebaseAuth.signOut()
    }
    override suspend fun getUserProfileImageByEmail(email: String): String? {
        val userCollection = firestore.collection("user")
        val documents = userCollection.whereEqualTo("email", email).get().await() // Use await to wait for the result

        if (!documents.isEmpty) {
            for (document in documents) {
                return document.getString("profileImageUrl") // Replace with your actual field name
            }
        }
        return null // Return null if no documents are found
    }
    override suspend fun getBacgroundByEmail(email: String): String? {
        val userCollection = firestore.collection("user")
        val documents = userCollection.whereEqualTo("email", email).get().await() // Use await to wait for the result

        if (!documents.isEmpty) {
            for (document in documents) {
                return document.getString("profileBacgroundImageUrl") // Replace with your actual field name
            }
        }
        return null // Return null if no documents are found
    }

}