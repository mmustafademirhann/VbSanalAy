package com.example.socialmediavbsanalay.data.dataSource.post

import com.example.socialmediavbsanalay.domain.model.Post

interface PostDataSource {
    suspend fun fetchPosts(): Result<List<Post>>
}