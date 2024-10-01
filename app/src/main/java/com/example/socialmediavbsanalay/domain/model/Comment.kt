package com.example.socialmediavbsanalay.domain.model

data class Comment(
    val postId: String = "",
    val userId: String = "",
    val comment: String = "",
    val username: String = "", // Kullanıcı adı
    val profileImageUrl: String = "", // Kullanıcı profil resmi URL'si
    val timestamp: Long = 0L,
    val commentId: String="",
)
