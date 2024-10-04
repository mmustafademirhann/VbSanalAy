package com.example.socialmediavbsanalay.domain.model

data class UserStories(
    val ownerUser: String, // Hikayenin sahibinin kullanıcı adı
    val stories: List<Story>, // Kullanıcının paylaştığı hikayelerin listesi
    val ownerUserProfileImage: String
)