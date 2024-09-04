package com.example.socialmediavbsanalay.data.repository.gallery

import com.example.socialmediavbsanalay.domain.model.Gallery
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    suspend fun getRecentPhotos(): List<Gallery>
}