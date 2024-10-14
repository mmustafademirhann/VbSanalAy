package com.example.socialmediavbsanalay.data.repositoryImpl.notification

import com.example.socialmediavbsanalay.data.dataSource.notification.NotificationDataSource
import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.data.repository.notification.NotificationRepository
import com.example.socialmediavbsanalay.domain.model.Notification
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource
) : NotificationRepository {

    override suspend fun addNotification(notification: Notification): Result<Boolean> {
        return notificationDataSource.addNotification(notification)
    }

    override suspend fun getNotifications(currentUserId: String): ApiResponse<List<Notification>> {
        return notificationDataSource.getNotifications(currentUserId)
    }
}