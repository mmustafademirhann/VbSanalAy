package com.example.socialmediavbsanalay.domain.model

data class Story(
    val id: String,
    val imageUrl: String = "",
    val ownerUser: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
