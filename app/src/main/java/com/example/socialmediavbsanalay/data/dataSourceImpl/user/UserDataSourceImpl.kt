package com.example.socialmediavbsanalay.data.dataSourceImpl.user

import com.example.socialmediavbsanalay.data.dataSource.user.UserDataSource
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val firestore:FirebaseFirestore

) : UserDataSource {

    // Reference to the "users" node in the Firebase Realtime Database
    private val usersRef: DatabaseReference = database.reference.child("users")

    override suspend fun addUser(user: User) {
        try {
            usersRef.child(user.id).setValue(user).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUser(userId: String): User? {
        return try {
            val snapshot = usersRef.child(userId).get().await()
            snapshot.getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllUsers(): List<User> {
        return try {
            val snapshot = usersRef.get().await()
            val users = mutableListOf<User>()
            snapshot.children.forEach { child ->
                val user = child.getValue(User::class.java)
                user?.let { users.add(it) }
            }
            users
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun searchUsers(query: String): Flow<List<User>> = flow {
        if (query.isNotBlank()) {
            val userList = mutableListOf<User>()
            val querySnapshot = firestore.collection("user")
                .whereGreaterThanOrEqualTo("id", query)
                .whereLessThanOrEqualTo("id", query + "\uf8ff")
                .get()
                .await()
            //neden return değilde flow kullandım ?
            for (document in querySnapshot.documents) {
                val user = document.toObject(User::class.java)
                if (user != null) {
                    userList.add(user)
                }
            }

            emit(userList)
        } else {
            emit(emptyList())
        }
    }

}
