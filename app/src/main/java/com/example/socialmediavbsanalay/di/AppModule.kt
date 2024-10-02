package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.post.StoryDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.post.StoryDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.post.StoryRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.post.StoryRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideStoryDataSource(
        firebaseFirestore: FirebaseFirestore
    ): StoryDataSource {
        return StoryDataSourceImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideStoryRepository(
        storyDataSource: StoryDataSource
    ): StoryRepository {
        return StoryRepositoryImpl(storyDataSource)
    }


}
