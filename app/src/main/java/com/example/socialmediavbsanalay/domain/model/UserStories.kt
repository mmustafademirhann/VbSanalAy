package com.example.socialmediavbsanalay.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserStories(
    val ownerUser: String, // Hikayenin sahibinin kullanıcı adı
    val stories: List<Story>, // Kullanıcının paylaştığı hikayelerin listesi
    val ownerUserProfileImage: String
): Parcelable