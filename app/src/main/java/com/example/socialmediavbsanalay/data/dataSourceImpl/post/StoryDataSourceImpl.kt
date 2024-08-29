package com.example.socialmediavbsanalay.data.dataSourceImpl.post

import com.example.socialmediavbsanalay.data.dataSource.post.StoryDataSource
import com.example.socialmediavbsanalay.domain.model.Story
import javax.inject.Inject

class StoryDataSourceImpl @Inject constructor() : StoryDataSource {

    override suspend fun fetchStories(): Result<List<Story>> {
        // Implement the actual fetching logic here
        // For example, you could fetch from a network or database
        return Result.success(emptyList()) // Replace with actual fetching logic
    }
}