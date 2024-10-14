package com.example.socialmediavbsanalay.domain.model

data class Notification(
    val ownerUserName: String,
    val userImage: String,
    val username: String,
    val notificationType: Int,
    val postId: String,
    val postImage: String,
    val isRead: Boolean
)