package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.post.PostDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.post.PostRepositoryImpl
import com.example.socialmediavbsanalay.domain.interactor.post.PostInteractor
import com.google.firebase.storage.FirebaseStorage
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
    fun providePostDataSource(
        firebaseStorage: FirebaseStorage
    ): PostDataSource {
        return PostDataSourceImpl(firebaseStorage)
    }

    @Provides
    @Singleton
    fun providePostRepository(
        postDataSource: PostDataSource
    ): PostRepository {
        return PostRepositoryImpl(postDataSource)
    }

}