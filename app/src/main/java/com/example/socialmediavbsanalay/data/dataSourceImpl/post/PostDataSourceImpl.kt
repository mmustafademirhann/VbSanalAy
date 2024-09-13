package com.example.socialmediavbsanalay.data.dataSourceImpl.post
import android.net.Uri
import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.domain.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firestore: FirebaseFirestore // Add Firestore dependency
): PostDataSource {

    // Collection where you will store the post metadata in Firestore
    private val postsCollection = firestore.collection("posts")

    override suspend fun fetchPosts(): Result<List<Post>> {
        // Fetch posts from Firestore
        return try {
            val snapshot = postsCollection.get().await()
            val posts = snapshot.documents.map { document ->
                val imageUrl = document.getString("imageUrl") ?: ""
                val username = document.getString("username") ?: "Unknown"
                Post(imageResId = imageUrl, username = username)
            }
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadPhoto(imageUri: Uri) {
        val storageRef = firebaseStorage.reference
        val photoRef = storageRef.child("photos/${UUID.randomUUID()}.jpg")

        // Upload the image to Firebase Storage
        val uploadTask = photoRef.putFile(imageUri).await()
        val downloadUrl = photoRef.downloadUrl.await().toString()

        // Now store the metadata (image URL, username, etc.) in Firestore
        val post = hashMapOf(
            "imageUrl" to downloadUrl,
            "username" to "SampleUser" // Replace this with the actual username
        )
        postsCollection.add(post).await() // Save post metadata to Firestore
    }

    override fun getPosts(): Flow<List<Post>> = callbackFlow {
        val listenerRegistration = postsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle the error (if any)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val posts = snapshot.documents.map { document ->
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val username = document.getString("username") ?: "Unknown"
                    Post(imageResId = imageUrl, username = username)
                }
                trySend(posts) // Use trySend to send the list of posts
            }
        }

        awaitClose { listenerRegistration.remove() } // Cleanup when the flow is closed
    }
}
