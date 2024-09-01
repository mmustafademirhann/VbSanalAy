package com.example.socialmediavbsanalay.data.repositoryImpl.gallery

import com.example.socialmediavbsanalay.data.dataSource.gallery.GalleryDataSource
import com.example.socialmediavbsanalay.data.repository.gallery.GalleryRepository
import com.example.socialmediavbsanalay.domain.model.Gallery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryDataSource: GalleryDataSource
) : GalleryRepository {
    override suspend fun getRecentPhotos(): List<Gallery> {
        return galleryDataSource.fetchRecentPhotos()
    }
}