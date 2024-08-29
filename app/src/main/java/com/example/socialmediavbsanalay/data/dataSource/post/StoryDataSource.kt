package com.example.socialmediavbsanalay.data.dataSource.post
import com.example.socialmediavbsanalay.domain.model.Story

interface StoryDataSource {
    suspend fun fetchStories(): Result<List<Story>>
}
