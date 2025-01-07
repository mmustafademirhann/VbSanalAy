package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.message.MessageDataSource
import com.example.socialmediavbsanalay.data.repository.message.MessageRepository
import com.example.socialmediavbsanalay.data.dataSourceImpl.message.MessageDataSourceImpl
import com.example.socialmediavbsanalay.data.repositoryImpl.message.MessageItemRepositoryImpl
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // This ensures that the module is available throughout the application
object MessageRepositoryModule {

    // Provide MessageDataSource
    @Provides
    fun provideMessageDataSource(): MessageDataSource {
        return MessageDataSourceImpl(firebaseFirestore = FirebaseFirestore.getInstance()) // Provide the concrete implementation of MessageDataSource
    }

    // Provide MessageRepository
    @Provides
    fun provideMessageRepository(messageDataSource: MessageDataSource): MessageRepository {
        return MessageItemRepositoryImpl(messageDataSource) // Provide the repository with the data source
    }
}
