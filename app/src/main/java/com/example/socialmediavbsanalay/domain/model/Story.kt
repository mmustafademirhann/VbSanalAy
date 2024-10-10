package com.example.socialmediavbsanalay.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String = "",
    var imageUrl: String = "",
    val ownerUser: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    var ownerUserProfileImage: String = "",
    var seenUsers: ArrayList<String>? = arrayListOf(),
    var storyExpireTime: Long = System.currentTimeMillis() + 86400
): Parcelable
