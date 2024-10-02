package com.example.socialmediavbsanalay.data.repository.post

import com.example.socialmediavbsanalay.domain.model.Story

interface StoryRepository {
    suspend fun getStories(): Result<List<Story>>
    suspend fun addStory(story: Story): Result<Unit>
}
