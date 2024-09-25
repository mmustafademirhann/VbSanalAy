package com.example.socialmediavbsanalay.presentation.viewModels

import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Global.putString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.domain.interactor.authentication.AuthInteractor
import com.example.socialmediavbsanalay.domain.interactor.gallery.GalleryInteractor
import com.example.socialmediavbsanalay.domain.interactor.post.PostInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.CreateUserInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.UserInteractor
import com.example.socialmediavbsanalay.domain.model.Gallery
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.adapters.GalleryAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.utils.SingleLiveData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryInteractor: GalleryInteractor,
    private val galleryAdapter: GalleryAdapter,
    private val postInteractor: PostInteractor,
    private val authInteractor: AuthInteractor,
    private val createUserInteractor:CreateUserInteractor,
    private val userInteractor: UserInteractor,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _recentPhotos = MutableLiveData<List<Gallery>>()
    val recentPhotos: LiveData<List<Gallery>> = _recentPhotos

    private val _uploadStatus = SingleLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts
    private var hasFetchedPosts = false

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> get() = _currentUser
    private val _userData = MutableLiveData<Result<User?>>()
    val userData: LiveData<Result<User?>> get() = _userData
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> get() = _userId
    var currentUserInId=""
    private val _userIdFlow = MutableStateFlow<String?>(null)
    val userIdFlow: StateFlow<String?> get() = _userIdFlow

    private val _updateProfileImageResult = MutableStateFlow<Result<Unit>?>(null)
    val updateProfileImageResult: StateFlow<Result<Unit>?> = _updateProfileImageResult

    private val _uploadStatuss = MutableLiveData<String>()
    val uploadStatuss: LiveData<String> get() = _uploadStatuss
    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> get() = _profileImageUrl






    var IDGET=""

    init {


        loadPosts() // Initialize posts LiveData when ViewModel is created
        fetchCurrentUserId()
    }
    //suspend fun fetchUserData(): User? {
        //return authInteractor.fetchUserData().getOrNull()
   // }
    fun uploadProfilePicturee(imageUri: Uri) {
        viewModelScope.launch {
            try {
                val imageUrl = galleryInteractor.uploadProfilePicture(imageUri)
                val userEmail = auth.currentUser?.email ?: throw Exception("E-posta alınamadı.")
                userInteractor.updateUserProfileImageByEmail(userEmail, imageUrl)
                _uploadStatuss.value = "Profil resmi güncellendi."
            } catch (e: Exception) {
                _uploadStatuss.value = "Hata: ${e.message}"
            }
        }

    }



    fun uploadProfilePicture(imageUri: Uri) {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            try {
                postInteractor.uploadPhoto(imageUri, userId)
                // Notify success or update LiveData to reflect changes
            } catch (e: Exception) {
                // Handle error (e.g., notify user)
            }
        }
    }
    fun updateUserProfileImage(email: String, imageUrl: String) {
        viewModelScope.launch {
            try {
                userInteractor.updateUserProfileImageByEmail(email, imageUrl)
                // Güncelleme başarılı ise uygun bir bildirim yapabilirsiniz
                _uploadStatuss.postValue("Profil resmi güncellendi.")
            } catch (e: Exception) {
                // Hata durumunda bildirim yapabilirsiniz
                _uploadStatuss.postValue(e.message)
            }
        }
    }
    fun fetchUserId(callback: (String?) -> Unit) {
        viewModelScope.launch {
            val userId = getUserIdByEmail() // Asynchronous call
            callback(userId) // Pass userId to the callback function
        }
    }
    private fun fetchCurrentUserId():String {

        return authInteractor.getCurrentUserId().toString().also { currentUserInId = it }


    }
    fun getUserById(userId: String) {
        viewModelScope.launch {

            val result = createUserInteractor.getUserById(userId)

            result.onSuccess { user ->
                _currentUser.value = user
            }.onFailure {
                _currentUser.value = null
            }
        }
    }



    fun loadPosts() {
        if (!hasFetchedPosts){
            viewModelScope.launch {
                try {
                    IDGET=getUserIdByEmail().toString()


                    val posts = postInteractor.getPosts() // Use the suspend function
                    _posts.value = posts
                    hasFetchedPosts = true
                } catch (e: Exception) {
                    _posts.value = emptyList() // Handle error case
                }
            }
        }

    }
    fun signOut() {
        authInteractor.signOut()
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
    fun fetchUserId() {
        viewModelScope.launch {
            val authId = fetchCurrentUserId()
            val userId = authInteractor.fetchUserIdByAuthId(authId)
            _userIdFlow.value = userId.toString() // Akışa kullanıcı ID'sini atıyoruz
        }
    }

    private suspend fun getUserIdByEmail(): String? {
        val email = authInteractor.getCurrentUserEmail()
        return email?.let { authInteractor.getUserIdByEmail(it) }
    }

    fun getUserId(): String? {
        return authInteractor.getCurrentUserEmail() // Return current user email directly or use getUserId
    }
    fun getCurrentUserId():String{

        return authInteractor.getCurrentUserId().toString()
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
