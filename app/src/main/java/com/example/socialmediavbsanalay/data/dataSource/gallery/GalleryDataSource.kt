package com.example.socialmediavbsanalay.data.dataSource.gallery

import com.example.socialmediavbsanalay.domain.model.Gallery
import kotlinx.coroutines.flow.Flow

interface GalleryDataSource {
    suspend fun fetchRecentPhotos(): List<Gallery>
}