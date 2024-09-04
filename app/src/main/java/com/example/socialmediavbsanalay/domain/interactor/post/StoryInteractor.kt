package com.example.socialmediavbsanalay.domain.interactor.post

import com.example.socialmediavbsanalay.data.repository.post.StoryRepository
import com.example.socialmediavbsanalay.domain.model.Story
import javax.inject.Inject

class StoryInteractor @Inject constructor(
    private val storyRepository: StoryRepository
) {

    suspend fun getStories(): Result<List<Story>> {
        return storyRepository.getStories()
    }
}