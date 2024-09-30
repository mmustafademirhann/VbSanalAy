package com.example.socialmediavbsanalay.domain.interactor.gallery

import android.net.Uri
import com.example.socialmediavbsanalay.data.repository.gallery.GalleryRepository
import com.example.socialmediavbsanalay.domain.model.Gallery
import javax.inject.Inject

class GalleryInteractorImpl @Inject constructor(
    private val galleryRepository: GalleryRepository
) : GalleryInteractor {
    override suspend fun fetchRecentPhotos(): List<Gallery> {
        return galleryRepository.getRecentPhotos()
    }
    override suspend fun uploadProfilePicture(imageUri: Uri): String {
        return galleryRepository.uploadProfilePicture(imageUri)
    }

    override suspend fun uploadBacground(imageUri: Uri): String {
        return galleryRepository.uploadBacground(imageUri)
    }
}