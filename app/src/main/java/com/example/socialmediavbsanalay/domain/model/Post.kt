package com.example.socialmediavbsanalay.domain.model

import java.util.Date

data class Post(
    val id: String,
    val imageResId: String, // Resource ID of the image for the story
    val username: String,  // Username of the user associated with the story
    val user: User?,
    var likesCount: Long,
    var commentsCount: Long,
    val likedBy: ArrayList<String> = arrayListOf(),
    val comments: List<Comment> = emptyList(), // List of comments
    val timestamp: Date?,
    val userProfileImage: String?,
)