package com.example.socialmediavbsanalay.data.repositoryImpl.post

import com.example.socialmediavbsanalay.data.dataSource.post.StoryDataSource
import com.example.socialmediavbsanalay.data.repository.post.StoryRepository
import com.example.socialmediavbsanalay.domain.model.Story
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val storyDataSource: StoryDataSource
) : StoryRepository {

    override suspend fun getStories(): Result<List<Story>> {
        return storyDataSource.fetchStories()
    }
}
