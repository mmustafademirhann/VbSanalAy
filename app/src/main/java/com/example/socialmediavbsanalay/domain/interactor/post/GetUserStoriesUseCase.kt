package com.example.socialmediavbsanalay.domain.interactor.post

import com.example.socialmediavbsanalay.data.repository.post.StoryRepository
import com.example.socialmediavbsanalay.domain.model.UserStories
import javax.inject.Inject

class GetUserStoriesUseCase@Inject constructor(private val storyRepository: StoryRepository) {
    suspend operator fun invoke(): List<UserStories> {
        return storyRepository.getUserStories()
    }
}
