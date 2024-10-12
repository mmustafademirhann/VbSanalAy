package com.example.socialmediavbsanalay.data.dataSourceImpl.post

import android.content.ContentValues.TAG
import android.util.Log
import com.example.socialmediavbsanalay.data.dataSource.post.CommentDataSource
import com.example.socialmediavbsanalay.domain.model.Comment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentDataSourceImpl @Inject constructor(private val firestore: FirebaseFirestore) : CommentDataSource {

    override suspend fun addComment(comment: Comment): Result<Unit> {
        return try {
            firestore.collection("comments").add(comment).await() // await ile asenkron işlemi bekleyin
            Result.success(Unit) // Başarılı durumda
        } catch (e: Exception) {
            Log.w(TAG, "Error adding comment", e)
            Result.failure(e) // Hata durumunda
        }
    }

    override fun getComments(postId: String): Flow<List<Comment>> {
        return callbackFlow {
            val listenerRegistration = firestore.collection("posts")
                .document(postId)
                .collection("comments")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }

                    val comments = snapshot?.toObjects(Comment::class.java) ?: emptyList()
                    trySend(comments).isSuccess
                }

            awaitClose { listenerRegistration.remove() }
        }
    }
    override fun fetchCommentsForPost(postId: String, onCommentsUpdated: (List<Comment>) -> Unit) {
        // Set up a snapshot listener for the comments related to the postId
        firestore.collection("comments")
            .whereEqualTo("postId", postId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("Firestore", "Error fetching comments: $exception")
                    onCommentsUpdated(emptyList()) // Return an empty list on error
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val comments = snapshot.documents.map { document ->
                        val id = document.id
                        val userId = document.getString("userId") ?: ""
                        val username = document.getString("username") ?: "Unknown" // Changed to username
                        val userProfil = document.getString("profileImageUrl") ?: "Unknown"
                        val content = document.getString("comment") ?: ""
                        val timestamp = document.getLong("timestamp") ?: 0L
                        Comment(
                            profileImageUrl = userProfil,
                            postId = id,
                            userId = userId,
                            username = username,
                            comment = content,
                            timestamp = timestamp
                        )
                    }

                    onCommentsUpdated(comments) // Call the callback with the fetched comments
                } else {
                    onCommentsUpdated(emptyList()) // Call the callback with an empty list if no comments found
                }
            }
    }


}
