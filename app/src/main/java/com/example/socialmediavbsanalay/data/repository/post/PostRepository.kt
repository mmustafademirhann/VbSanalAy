package com.example.socialmediavbsanalay.data.repository.post

import android.net.Uri
import com.example.socialmediavbsanalay.domain.model.Post

import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<List<Post>>
    suspend fun uploadPhoto(imageUri: Uri): Unit

}