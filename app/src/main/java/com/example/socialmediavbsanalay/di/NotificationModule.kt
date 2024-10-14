package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.notification.NotificationDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.notification.NotificationDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.notification.NotificationRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.notification.NotificationRepositoryImpl
import com.example.socialmediavbsanalay.domain.interactor.notification.NotificationInteractor
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun providesNotificationDataSource(
        firebaseFirestore: FirebaseFirestore
    ): NotificationDataSource {
        return NotificationDataSourceImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun providesNotificationRepository(
        notificationDataSource: NotificationDataSource
    ): NotificationRepository {
        return NotificationRepositoryImpl(notificationDataSource)
    }

    @Provides
    @Singleton
    fun providesNotificationInteractor(
        notificationRepository: NotificationRepository
    ): NotificationInteractor {
        return NotificationInteractor(notificationRepository)
    }
}