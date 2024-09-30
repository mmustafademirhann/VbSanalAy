package com.example.socialmediavbsanalay.domain.model

data class Post(
    val imageResId: String, // Resource ID of the image for the story
    val username: String ,  // Username of the user associated with the story
    val user: User?
     // URL of the user's profile photo
)