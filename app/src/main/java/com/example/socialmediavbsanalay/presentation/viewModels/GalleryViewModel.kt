package com.example.socialmediavbsanalay.presentation.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.domain.interactor.gallery.GalleryInteractor
import com.example.socialmediavbsanalay.domain.interactor.post.PostInteractor
import com.example.socialmediavbsanalay.domain.model.Gallery
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.presentation.adapters.GalleryAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryInteractor: GalleryInteractor,
    val galleryAdapter: GalleryAdapter, // Injected here
    private val postInteractor: PostInteractor
) : ViewModel() {

    private val _recentPhotos = MutableLiveData<List<Gallery>>()
    val recentPhotos: LiveData<List<Gallery>> = _recentPhotos
    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus
    val posts: LiveData<List<Post>> = postInteractor.getPosts()
        .asLiveData()



    fun loadGalleryImages(context: Context) {
        viewModelScope.launch {
            val photos = galleryInteractor.fetchRecentPhotos()
            _recentPhotos.value = photos
            galleryAdapter.submitList(photos)
        }
    }
    fun uploadPhoto(imageUri: Uri) {
        viewModelScope.launch {
            try {
                val photoUrl = postInteractor.uploadPhoto(imageUri)
                _uploadStatus.value = "Upload successful: $photoUrl"
            } catch (e: Exception) {
                _uploadStatus.value = "Upload failed: ${e.message}"
            }
        }
    }
    //izin
}