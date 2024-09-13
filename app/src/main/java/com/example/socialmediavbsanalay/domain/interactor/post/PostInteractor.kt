package com.example.socialmediavbsanalay.domain.interactor.post

import android.net.Uri
import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.domain.model.Story
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostInteractor @Inject constructor(
    private val postRepository: PostRepository
){
    fun getPosts(): Flow<List<Post>> {
        return postRepository.getPosts()
    }
    suspend fun uploadPhoto(imageUri: Uri): Unit {
        return postRepository.uploadPhoto(imageUri)
    }
}
