package com.example.socialmediavbsanalay.data.dataSourceImpl.post

import android.net.Uri
import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.domain.model.Post
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage
):PostDataSource {
    override suspend fun fetchPosts(): Result<List<Post>> {
        // Implement the actual fetching logic here
        // For example, you could fetch from a network or database
        return Result.success(emptyList()) // Replace with actual fetching logic
    }
    override suspend fun uploadPhoto(imageUri: Uri): Unit {
        val storageRef = firebaseStorage.reference
        val photoRef = storageRef.child("photos/${imageUri.lastPathSegment}")

        photoRef.putFile(imageUri)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener {
                // Handle failure
            }
    }
    override fun getPosts(): Flow<List<Post>> = flow {
        try {
            val storageRef = firebaseStorage.reference.child("photos")
            val listResult = storageRef.listAll().await()
            val posts = listResult.items.map { item ->
                val imageUrl = item.downloadUrl.await().toString()
                // Create a Post object with the image URL
                Post(imageResId = imageUrl, username = "Unknown") // Replace "Unknown" with real username if available
            }
            emit(posts)
        } catch (e: Exception) {
            // Handle error
            emit(emptyList())
        }
    }

}