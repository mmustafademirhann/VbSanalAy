package com.example.socialmediavbsanalay.data.dataSource.gallery

import android.net.Uri
import com.example.socialmediavbsanalay.domain.model.Gallery


interface GalleryDataSource {
    suspend fun fetchRecentPhotos(): List<Gallery>
    suspend fun uploadProfilePicture(imageUri: Uri): String
    suspend fun uploadBacground(imageUri: Uri): String
}