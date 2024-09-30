package com.example.socialmediavbsanalay.data.repository.gallery

import android.net.Uri
import com.example.socialmediavbsanalay.domain.model.Gallery

interface GalleryRepository {
    suspend fun getRecentPhotos(): List<Gallery>
    suspend fun uploadProfilePicture(imageUri: Uri): String
}