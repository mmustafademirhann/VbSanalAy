package com.example.socialmediavbsanalay.data.repository.post

import android.net.Uri
import com.example.socialmediavbsanalay.domain.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun uploadPhoto(imageUri: Uri,userId: String): Unit

    fun likePost(postId: String, userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)


}