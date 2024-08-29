package com.example.socialmediavbsanalay.data.dataSourceImpl.post

import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.domain.model.Post
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor():PostDataSource {
    override suspend fun fetchPosts(): Result<List<Post>> {
        // Implement the actual fetching logic here
        // For example, you could fetch from a network or database
        return Result.success(emptyList()) // Replace with actual fetching logic
    }
}