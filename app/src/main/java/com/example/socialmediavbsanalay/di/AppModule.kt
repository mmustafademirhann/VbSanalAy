package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.authentication.FirebaseAuthDataSource
import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.data.dataSource.post.StoryDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.authentication.FirebaseAuthDataSourceImpl
import com.example.socialmediavbsanalay.data.dataSourceImpl.post.PostDataSourceImpl
import com.example.socialmediavbsanalay.data.dataSourceImpl.post.StoryDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.authentication.AuthRepository
import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.data.repository.post.StoryRepository

import com.example.socialmediavbsanalay.data.repositoryImpl.authentication.AuthRepositoryImpl
import com.example.socialmediavbsanalay.data.repositoryImpl.post.PostRepositoryImpl
import com.example.socialmediavbsanalay.data.repositoryImpl.post.StoryRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
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
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthDataSource(
        firebaseAuth: FirebaseAuth
    ): FirebaseAuthDataSource {
        return FirebaseAuthDataSourceImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuthDataSource: FirebaseAuthDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuthDataSource)
    }

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
