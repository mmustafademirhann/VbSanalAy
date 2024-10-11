package com.example.socialmediavbsanalay.domain.model

import java.util.Date

data class Post(
    val id: String,
    val imageResId: String, // Resource ID of the image for the story
    val username: String ,  // Username of the user associated with the story
    val user: User?,
    val likes: Int = 0,
    val likedBy: List<String> = listOf(),
    val comments: List<Comment> = emptyList(), // List of comments
    val timestamp: Date?
    // URL of the user's profile photo
)