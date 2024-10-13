package com.example.socialmediavbsanalay.data.dataSourceImpl.notification

import com.example.socialmediavbsanalay.data.dataSource.notification.NotificationDataSource
import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.domain.model.Notification
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
): NotificationDataSource {

    override suspend fun addNotification(notification: Notification): Result<Boolean> {
        try {
            val notificationCollection = fireStore.collection("notifications")

            val notificationData = mapOf(
                "ownerUserName" to notification.ownerUserName,
                "userName" to notification.username,
                "notificationType" to notification.notificationType,
                "postId" to notification.postId,
                "postImage" to notification.postImage,
                "isRead" to notification.isRead,
                "userImage" to notification.userImage
            )

            notificationCollection.add(notificationData).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getNotifications(currentUserId: String): ApiResponse<List<Notification>> {
        return try {
            val notificationCollection = fireStore.collection("notifications")

            // Fetch the notifications from Firestore where ownerUserName matches currentUserId
            val snapshot = notificationCollection
                .whereEqualTo("ownerUserName", currentUserId)
                .get()
                .await()

            // Map the snapshot documents to Notification objects
            val notifications = snapshot.documents.map { document ->
                Notification(
                    ownerUserName = document.getString("ownerUserName") ?: "",
                    userImage = document.getString("userImage") ?: "",
                    username = document.getString("userName") ?: "",
                    notificationType = document.getLong("notificationType")?.toInt() ?: 0,
                    postId = document.getString("postId") ?: "",
                    postImage = document.getString("postImage") ?: "",
                    isRead = document.getBoolean("isRead") ?: false
                )
            }

            ApiResponse.Success(notifications)
        } catch (e: Exception) {
            ApiResponse.Fail(e)
        }
    }

}