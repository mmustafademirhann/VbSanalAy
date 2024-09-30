package com.example.socialmediavbsanalay.domain.interactor.gallery

import android.net.Uri
import com.example.socialmediavbsanalay.domain.model.Gallery

interface GalleryInteractor {
    suspend fun fetchRecentPhotos(): List<Gallery>
    suspend fun uploadProfilePicture(imageUri: Uri): String
}
