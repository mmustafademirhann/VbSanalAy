package com.example.socialmediavbsanalay.data.dataSource.post

import android.net.Uri
import com.example.socialmediavbsanalay.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostDataSource {
    suspend fun fetchPosts(): Result<List<Post>>
    suspend fun uploadPhoto(imageUri: Uri,userId: String): Unit
    suspend fun getPosts(): List<Post>

}