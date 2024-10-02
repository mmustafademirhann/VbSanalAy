package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.data.dataSource.post.CommentDataSource
import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.data.dataSource.user.CreateUserDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.post.CommentDataSourceImpl
import com.example.socialmediavbsanalay.data.dataSourceImpl.post.PostDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.post.CommentRepository
import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.post.CommentRepositoryImpl
import com.example.socialmediavbsanalay.data.repositoryImpl.post.PostRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
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
        firebaseStorage: FirebaseStorage,
        firebaseFirestore: FirebaseFirestore,
        createUserDataSource: CreateUserDataSource,
        userRepository: UserRepository,
        userPreferences: UserPreferences
    ): PostDataSource {
        return PostDataSourceImpl(firebaseStorage,firebaseFirestore,createUserDataSource,userRepository,userPreferences)
    }

    @Provides
    @Singleton
    fun providePostRepository(
        postDataSource: PostDataSource
    ): PostRepository {
        return PostRepositoryImpl(postDataSource)
    }

    @Provides
    @Singleton
    fun provideCommentDataSource(
        firestore: FirebaseFirestore
    ): CommentDataSource {
        return CommentDataSourceImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideCommentRepository(commentDataSource: CommentDataSource): CommentRepository {
        return CommentRepositoryImpl(commentDataSource) // CommentRepositoryImpl örneğini sağlar
    }

}