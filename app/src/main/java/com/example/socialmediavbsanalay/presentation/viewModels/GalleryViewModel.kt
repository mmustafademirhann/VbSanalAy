package com.example.socialmediavbsanalay.presentation.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.domain.interactor.authentication.AuthInteractor
import com.example.socialmediavbsanalay.domain.interactor.gallery.GalleryInteractor
import com.example.socialmediavbsanalay.domain.interactor.post.PostInteractor
import com.example.socialmediavbsanalay.domain.model.Gallery
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.presentation.adapters.GalleryAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryInteractor: GalleryInteractor,
    private val galleryAdapter: GalleryAdapter,
    private val postInteractor: PostInteractor,
    private val authInteractor: AuthInteractor
) : ViewModel() {

    private val _recentPhotos = MutableLiveData<List<Gallery>>()
    val recentPhotos: LiveData<List<Gallery>> = _recentPhotos

    private val _uploadStatus = SingleLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts
    private var hasFetchedPosts = false

    var x=""

    init {
        loadPosts() // Initialize posts LiveData when ViewModel is created
    }

    fun loadPosts() {
        if (!hasFetchedPosts){
            viewModelScope.launch {
                try {
                    x=getUserIdByEmail().toString()
                    val posts = postInteractor.getPosts() // Use the suspend function
                    _posts.value = posts
                    hasFetchedPosts = true
                } catch (e: Exception) {
                    _posts.value = emptyList() // Handle error case
                }
            }
        }

    }

    suspend fun refreshGallery() {
        try {
            val photos = postInteractor.getPosts()
            _posts.value = photos
        } catch (e: Exception) {
            _posts.value = emptyList()
        }
    }

    fun loadGalleryImages() {
        viewModelScope.launch {
            try {
                val photos = galleryInteractor.fetchRecentPhotos()
                _recentPhotos.value = photos
                galleryAdapter.submitList(photos) // Update GalleryAdapter with new photos
            } catch (e: Exception) {
                _recentPhotos.value = emptyList() // Handle error by setting empty list
            }
        }
    }


    private suspend fun getUserIdByEmail(): String? {
        val email = authInteractor.getCurrentUserEmail()
        return email?.let { authInteractor.getUserIdByEmail(it) }
    }

    fun getUserId(): String? {
        return authInteractor.getCurrentUserEmail() // Return current user email directly or use getUserId
    }

    fun uploadPhoto(imageUri: Uri) {
        viewModelScope.launch {
            try {
                val userId = getUserIdByEmail()
                if (userId != null) {
                    postInteractor.uploadPhoto(imageUri, userId)
                    _uploadStatus.value = "Upload Successful"
                } else {
                    _uploadStatus.value = "User ID not found"
                }
            } catch (e: Exception) {
                _uploadStatus.value = "Upload Failed: ${e.message}"
            }
        }
    }

}
