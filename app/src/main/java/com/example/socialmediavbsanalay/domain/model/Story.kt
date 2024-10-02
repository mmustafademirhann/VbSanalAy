package com.example.socialmediavbsanalay.domain.model

data class Story(
    val id: String,
    val ownerUser: String,
    val imageUrl: String,
    val profileImageUrl: String, // Profil fotoğrafı URL'si
    val timestamp: Long // Zaman damgası, hikayenin paylaşım zamanını tutar
)
