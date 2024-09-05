package com.example.socialmediavbsanalay.data.dataSourceImpl.user

import com.example.socialmediavbsanalay.data.dataSource.user.CreateUserDataSource
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CreateUserDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CreateUserDataSource {

    override suspend fun createUser(userId: String, user: User) = suspendCoroutine { continuation ->
        firestore.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                continuation.resume(Unit)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
}