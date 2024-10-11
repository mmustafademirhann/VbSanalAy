package com.example.socialmediavbsanalay.domain.interactor.post

import com.example.socialmediavbsanalay.data.repository.post.StoryRepository
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.UserStories
import javax.inject.Inject

class StoryInteractor @Inject constructor(
    private val repository: StoryRepository
) {
    suspend fun fetchStories(): Result<List<Story>> {
        return repository.getStories()
    }

    suspend fun uploadStory(story: Story): Result<Unit> {
        return repository.addStory(story)
    }

    suspend fun updateSeenStatusOfStory(story: Story?, currentUser: String): Result<Unit> {
        return repository.updateSeenStatusOfStory(story, currentUser)
    }
}
