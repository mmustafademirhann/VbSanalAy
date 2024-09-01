package com.example.socialmediavbsanalay.domain.interactor.gallery

import com.example.socialmediavbsanalay.data.repository.gallery.GalleryRepository
import com.example.socialmediavbsanalay.domain.model.Gallery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryInteractorImpl @Inject constructor(
    private val galleryRepository: GalleryRepository
) : GalleryInteractor {
    override suspend fun fetchRecentPhotos(): List<Gallery> {
        return galleryRepository.getRecentPhotos()
    }
}