package com.example.socialmediavbsanalay.domain.interactor.notification

import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.data.repository.notification.NotificationRepository
import com.example.socialmediavbsanalay.domain.model.Notification
import javax.inject.Inject

class NotificationInteractor @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    suspend fun addNotification(notification: Notification): Result<Boolean> {
        return notificationRepository.addNotification(notification)
    }

    suspend fun getNotifications(currentUserId: String): ApiResponse<List<Notification>> {
        return notificationRepository.getNotifications(currentUserId)
    }
}