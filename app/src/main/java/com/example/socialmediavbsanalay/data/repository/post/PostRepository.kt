package com.example.socialmediavbsanalay.data.repository.post

import android.net.Uri
import com.example.socialmediavbsanalay.domain.model.Post

interface PostRepository {
    suspend fun getPosts(
        followingList: List<String>,
        onPostsUpdated: (List<Post>) -> Unit
    )
    suspend fun uploadPhoto(imageUri: Uri,userId: String): Result<Boolean>

    fun likeOrUnlikePost(postId: String, userId: String, isLike: Boolean, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    suspend fun getPostsByFollowingUsers(followingList: List<String>): List<Post>


}