package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.post.PostDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.post.PostRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {

    @Provides
    @Singleton
    fun providePostDataSource(): PostDataSource {
        return PostDataSourceImpl()
    }

    @Provides
    @Singleton
    fun providePostRepository(
        postDataSource: PostDataSource
    ): PostRepository {
        return PostRepositoryImpl(postDataSource)
    }
}