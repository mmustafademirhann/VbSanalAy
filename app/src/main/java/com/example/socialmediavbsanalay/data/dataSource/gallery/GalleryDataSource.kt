package com.example.socialmediavbsanalay.data.dataSource.gallery

import android.net.Uri
import com.example.socialmediavbsanalay.domain.model.Gallery
import kotlinx.coroutines.flow.Flow

interface GalleryDataSource {
    suspend fun fetchRecentPhotos(): List<Gallery>
    suspend fun uploadProfilePicture(imageUri: Uri): String
}