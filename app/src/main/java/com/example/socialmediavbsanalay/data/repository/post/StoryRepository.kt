package com.example.socialmediavbsanalay.data.repository.post

import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.UserStories

interface StoryRepository {
    suspend fun getStories(): Result<List<Story>>
    suspend fun addStory(story: Story): Result<Unit>
    suspend fun getUserStories(): List<UserStories>
}
