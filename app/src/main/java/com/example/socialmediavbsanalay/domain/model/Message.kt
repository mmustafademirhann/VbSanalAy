package com.example.socialmediavbsanalay.domain.model

data class Message(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: Long
)
