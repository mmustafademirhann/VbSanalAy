package com.example.socialmediavbsanalay.data.repository.post

import com.example.socialmediavbsanalay.domain.model.Post

interface PostRepository {
    suspend fun getPosts(): Result<List<Post>>
}