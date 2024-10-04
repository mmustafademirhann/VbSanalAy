package com.example.socialmediavbsanalay.data.dataSourceImpl.user

import android.util.Log
import com.example.socialmediavbsanalay.data.dataSource.user.UserDataSource
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldPath
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
    fun getUserProfileImageByEmail(email: String): String? {
        // Return a Flow or a suspend function to fetch the data
        var imageUrl: String? = null
        val userCollection = firestore.collection("user")

        // Assuming the user document is named by their email or some unique identifier
        userCollection.whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        imageUrl = document.getString("profileImageUrl") // Replace with your actual field name
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("UserInteractor", "Error getting user profile image: ${exception.message}")
            }

        return imageUrl // This might be null until the asynchronous call completes
    }
    override suspend fun updateUserProfileImageByEmail(email: String, imageUrl: String) {
        val query = firestore.collection("user").whereEqualTo("email", email).get().await()
        if (!query.isEmpty) {
            val document = query.documents[0]
            document.reference.update("profileImageUrl", imageUrl).await()
        } else {
            throw Exception("Kullanıcı bulunamadı.")
        }
    }

    override suspend fun updateBacgroundByEmail(email: String, imageUrl: String) {
        val query = firestore.collection("user").whereEqualTo("email", email).get().await()
        if (!query.isEmpty) {
            val document = query.documents[0]
            document.reference.update("profileBacgroundImageUrl", imageUrl).await()
        } else {
            throw Exception("Kullanıcı bulunamadı.")
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

    override suspend fun updateUserProfileImage(userId: String, imageUrl: String) {

        firestore.collection("users").document(userId).update("profileImageUrl", imageUrl).await()

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
    override suspend fun getUsersByIds(userIds: List<String>): Map<String, User> {
        // Firestore sorgusunu oluşturun
        val query = firestore.collection("users")
            .whereIn(FieldPath.documentId(), userIds)
            .get()

        // Kullanıcı bilgilerini döndürün
        return query.await().associate { document ->
            val user = document.toObject(User::class.java)
            user.id to user
        }
    }
    override suspend fun getUsersWithSharedStories(): List<User> {
        return try {
            val snapshot = firestore.collection("users") // "users" koleksiyonu kullanılıyor
                .whereArrayContains("stories", true) // Paylaşılan hikayeleri almak için filtre
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                val id = document.id
                val name = document.getString("name") ?: return@mapNotNull null
                val surName = document.getString("surName") ?: return@mapNotNull null
                val email = document.getString("email") ?: return@mapNotNull null
                val gender = document.getString("gender") ?: return@mapNotNull null
                val profileImageUrl = document.getString("profileImageUrl") ?: return@mapNotNull null
                val profileBackgroundImageUrl = document.getString("profileBacgroundImageUrl") ?: return@mapNotNull null

                val stories = document.get("stories") as? List<Map<String, Any>> ?: listOf()
                val storyList = stories.map { storyMap ->
                    Story(
                        id = storyMap["id"] as? String ?: "",
                        timestamp = storyMap["timestamp"] as? Long ?: 0
                    )
                }

                User(
                    id = id,
                    name = name,
                    surName = surName,
                    email = email,
                    gender = gender,
                    profileImageUrl = profileImageUrl,
                    profileBacgroundImageUrl = profileBackgroundImageUrl,
                    stories = storyList
                )
            }
        } catch (e: Exception) {
            emptyList() // Hata durumunda boş liste döndür
        }
    }


}
