package com.example.socialmediavbsanalay.domain.interactor.post

import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.domain.model.Story
import javax.inject.Inject

class PostInteractor @Inject constructor(
    private val postRepository: PostRepository
){
    suspend fun getPosts(): Result<List<Post>> {
        return postRepository.getPosts()
    }
}
