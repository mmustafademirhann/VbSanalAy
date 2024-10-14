package com.example.socialmediavbsanalay.data.repository.notification

import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.domain.model.Notification

interface NotificationRepository {

    suspend fun addNotification(notification: Notification): Result<Boolean>

    suspend fun getNotifications(currentUserId: String): ApiResponse<List<Notification>>
}