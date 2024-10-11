package com.example.socialmediavbsanalay.data.dataSource.post
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.UserStories

interface StoryDataSource {
    suspend fun fetchStories(): Result<List<Story>>
    suspend fun uploadStory(story: Story): Result<Unit>
    suspend fun getAllStories(): ArrayList<UserStories>

    suspend fun updateSeenStatusOfStory(story: Story?, currentUser: String): Result<Unit>
}
