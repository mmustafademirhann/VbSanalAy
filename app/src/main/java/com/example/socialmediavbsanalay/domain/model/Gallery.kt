package com.example.socialmediavbsanalay.domain.model

import android.net.Uri


data class Gallery(
    val id: Long,
    val name: String,
    val uri: Uri,
    val dateAdded: Long,
    val path: String
)
