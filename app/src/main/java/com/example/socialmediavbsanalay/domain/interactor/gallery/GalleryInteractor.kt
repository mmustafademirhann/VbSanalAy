package com.example.socialmediavbsanalay.domain.interactor.gallery

import com.example.socialmediavbsanalay.domain.model.Gallery
import kotlinx.coroutines.flow.Flow

interface GalleryInteractor {
    suspend fun fetchRecentPhotos(): List<Gallery>
}
