package com.example.socialmediavbsanalay.data.dataSourceImpl.user

import android.util.Log
import com.example.socialmediavbsanalay.data.dataSource.user.CreateUserDataSource
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CreateUserDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CreateUserDataSource {

    override suspend fun createUser(userId: String, user: User) = suspendCoroutine { continuation ->
        firestore.collection("user").document(userId)
            .set(user)
            .addOnSuccessListener {
                continuation.resume(Unit)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    override suspend fun checkIfUserExists(id: String): Boolean = suspendCoroutine { continuation ->
        firestore.collection("user")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    continuation.resume(true)
                    //I am using this also here ?
                } else {
                    continuation.resume(false)
                }
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
    override suspend fun getUserById(userId: String): Result<User?> {
        val documentSnapshot = firestore.collection("user").document(userId).get().await()
        return if (documentSnapshot.exists()) {
            Result.success(documentSnapshot.toObject(User::class.java))
        } else {
            Result.success(null)
        }
    }


}