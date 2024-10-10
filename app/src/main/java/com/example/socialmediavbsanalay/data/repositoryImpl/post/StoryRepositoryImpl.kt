package com.example.socialmediavbsanalay.data.repositoryImpl.post

import com.example.socialmediavbsanalay.data.dataSource.post.StoryDataSource
import com.example.socialmediavbsanalay.data.repository.post.StoryRepository
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.UserStories
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val dataSource: StoryDataSource
) : StoryRepository {

    override suspend fun getStories(): Result<List<Story>> {
        return dataSource.fetchStories()
    }

    override suspend fun addStory(story: Story): Result<Unit> {
        return dataSource.uploadStory(story)
    }
    override suspend fun getUserStories(): ArrayList<UserStories> {
        return dataSource.getAllStories()
    }

    override suspend fun updateSeenStatusOfStory(story: Story?, currentUser: String): Result<Unit> {
        return dataSource.updateSeenStatusOfStory(story, currentUser)
    }
}