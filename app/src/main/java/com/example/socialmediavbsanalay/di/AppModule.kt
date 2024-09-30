package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.post.StoryDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.post.StoryDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.post.StoryRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.post.StoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStoryDataSource(): StoryDataSource {
        return StoryDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideStoryRepository(
        storyDataSource: StoryDataSource
    ): StoryRepository {
        return StoryRepositoryImpl(storyDataSource)
    }


}
