package com.example.socialmediavbsanalay.data.repository.post

import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.UserStories

interface StoryRepository {
    suspend fun getStories(): Result<List<Story>>
    suspend fun addStory(story: Story): Result<Unit>
    suspend fun getUserStories(followingList: List<String>): ArrayList<UserStories>

    suspend fun updateSeenStatusOfStory(story: Story?, currentUser: String): Result<Unit>
}
