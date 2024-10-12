package com.example.socialmediavbsanalay.data.dataSource.post

import android.net.Uri
import com.example.socialmediavbsanalay.domain.model.Post

interface PostDataSource {
    suspend fun uploadPhoto(imageUri: Uri,userId: String): Result<Boolean>
    suspend fun getPosts(
        followingList: List<String>,
        onPostsUpdated: (List<Post>) -> Unit
    )
    fun likeOrUnlikePost(postId: String, userId: String, isLike: Boolean, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    suspend fun fetchFollowedUsersPosts(followingList: List<String>): List<Post>

}