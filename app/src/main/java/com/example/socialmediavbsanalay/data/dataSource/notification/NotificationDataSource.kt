package com.example.socialmediavbsanalay.data.dataSource.notification

import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.domain.model.Notification

interface NotificationDataSource {

    suspend fun addNotification(notification: Notification): Result<Boolean>

    suspend fun getNotifications(currentUserId: String): ApiResponse<List<Notification>>
}
