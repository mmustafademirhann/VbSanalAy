package com.example.socialmediavbsanalay.data.repositoryImpl.post

import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.domain.model.Post
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource
): PostRepository {
    override suspend fun getPosts(): Result<List<Post>> {
        // Implement the actual fetching logic here
        // For example, you could fetch from a network or database
        return postDataSource.fetchPosts() // Replace with actual fetching logic
    }
}