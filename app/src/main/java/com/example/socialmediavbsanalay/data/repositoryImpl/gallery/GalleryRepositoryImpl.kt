package com.example.socialmediavbsanalay.data.repositoryImpl.gallery

import android.net.Uri
import com.example.socialmediavbsanalay.data.dataSource.gallery.GalleryDataSource
import com.example.socialmediavbsanalay.data.repository.gallery.GalleryRepository
import com.example.socialmediavbsanalay.domain.model.Gallery
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryDataSource: GalleryDataSource
) : GalleryRepository {
    override suspend fun getRecentPhotos(): List<Gallery> {
        return galleryDataSource.fetchRecentPhotos()
    }

    override suspend fun uploadProfilePicture(imageUri: Uri): String {
        return galleryDataSource.uploadProfilePicture(imageUri)
    }

    override suspend fun uploadBacground(imageUri: Uri): String {
        return galleryDataSource.uploadBacground(imageUri)
    }
}