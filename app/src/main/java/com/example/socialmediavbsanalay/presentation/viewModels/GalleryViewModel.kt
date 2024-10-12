package com.example.socialmediavbsanalay.presentation.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.domain.interactor.CommentInteractor
import com.example.socialmediavbsanalay.domain.interactor.authentication.AuthInteractor
import com.example.socialmediavbsanalay.domain.interactor.gallery.GalleryInteractor
import com.example.socialmediavbsanalay.domain.interactor.post.PostInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.CreateUserInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.UserInteractor
import com.example.socialmediavbsanalay.domain.model.Comment
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
    private val auth: FirebaseAuth,
    private val commentInteractor: CommentInteractor,
    private val userPreferences: UserPreferences,



) : ViewModel() {

    private val _recentPhotos = MutableLiveData<List<Gallery>>()
    val recentPhotos: LiveData<List<Gallery>> = _recentPhotos

    private val _uploadStatus = SingleLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    private val _postUploadStatus = SingleLiveData<ApiResponse<Boolean>>()
    val postUploadStatus: SingleLiveData<ApiResponse<Boolean>> get() = _postUploadStatus

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
    private val _uploadbStatuss = MutableLiveData<String>()
    val uploadbStatuss: LiveData<String> get() = _uploadbStatuss


    private val _profileImageUrl = MutableLiveData<String?>()
    val profileImageUrl: LiveData<String?> get() = _profileImageUrl

    private val _profilebImageUrl = MutableLiveData<String?>()
    val profilebImageUrl: LiveData<String?> get() = _profilebImageUrl

    private val _currentImageUrl = MutableLiveData<String>() // LiveData for current image URL
    val currentImageUrl: LiveData<String> get() = _currentImageUrl
    private val _currentbImageUrl = MutableLiveData<String>() // LiveData for current image URL
    val currentbImageUrl: LiveData<String> get() = _currentbImageUrl

    private val _addCommentStatus = MutableLiveData<Result<Unit>>()
    val addCommentStatus: LiveData<Result<Unit>> get() = _addCommentStatus

    // Yorumları almak için LiveData
    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    var IDGET=""
    val postss = MutableLiveData<List<Post>>()
    private var currentUserFollowingList: List<String> = emptyList()


    // Takip edilen kullanıcıların postlarını getir
    fun fetchFollowedUsersPosts(followingList: List<String>) {
        viewModelScope.launch {
            val followedPosts = postInteractor.executePost(followingList)
            postss.postValue(followedPosts)
        }
    }

    fun loadComments(postId: String): Flow<List<Comment>> {
        return commentInteractor.getComments(postId)
    }
    fun fetchComments(postId: String) {
        commentInteractor.loadComments(postId) { updatedComments ->
            _comments.value = updatedComments
        }
    }



    // Yorum eklemek için fonksiyon
    fun addComment(comment: Comment) {
        viewModelScope.launch {
            val result = commentInteractor.addComment(comment)
            _addCommentStatus.value = result
            loadComments(comment.postId)
        }
    }
    init {
        currentUserFollowingList = userPreferences.getUser()?.following ?: listOf()
        loadPosts() // Initialize posts LiveData when ViewModel is created
        fetchCurrentUserId()
    }
    //suspend fun fetchUserData(): User? {
    //return authInteractor.fetchUserData().getOrNull()
    // }
    fun commentOnPost(postId: String, commentText: String, userId: String) {
        val comment = Comment(
            userId = userId,
            postId = postId,
            comment = commentText
        )

        // Firestore'a yorum ekleme işlemi
       addComment(comment)
    }

    fun uploadProfilePicturee(imageUri: Uri) {
        viewModelScope.launch {
            try {
                // Check if the current image URL is already set
                if (_currentImageUrl.value == null) {
                    val imageUrl = galleryInteractor.uploadProfilePicture(imageUri)
                    val userEmail = auth.currentUser?.email ?: throw Exception("E-posta alınamadı.")
                    userInteractor.updateUserProfileImageByEmail(userEmail, imageUrl)
                    _currentImageUrl.value = imageUrl // Update current image URL
                    _uploadStatuss.value = "Profil resmi güncellendi."
                } else {
                    _uploadStatuss.value = "Profil resmi zaten güncellenmiş."
                }
            } catch (e: Exception) {
                _uploadStatuss.value = "Hata: ${e.message}"
            }
        }
    }
    fun getUserProfileImage(email: String) {
        viewModelScope.launch {
            val result = authInteractor.getUserProfileImageByEmail(email)

            // Extract the value from the Result and post it to LiveData
            _profileImageUrl.postValue(result.getOrNull()) // This will be String? or null
        }
    }
    fun uploadBacgroundPicturee(imageUri: Uri) {
        viewModelScope.launch {
            try {
                // Check if the current image URL is already set
                if (_currentbImageUrl.value == null) {
                    val imageUrl = galleryInteractor.uploadBacground(imageUri)
                    val userEmail = auth.currentUser?.email ?: throw Exception("E-posta alınamadı.")
                    userInteractor.updateBacgroundByEmail(userEmail, imageUrl)
                    _currentbImageUrl.value = imageUrl // Update current image URL
                    _uploadbStatuss.value = "Profil resmi güncellendi."
                } else {
                    _uploadbStatuss.value = "Profil resmi zaten güncellenmiş."
                }
            } catch (e: Exception) {
                _uploadbStatuss.value = "Hata: ${e.message}"
            }
        }
    }
    fun getBacgroudImage(email: String) {
        viewModelScope.launch {
            val result = authInteractor.getBacgroundByEmail(email)

            // Extract the value from the Result and post it to LiveData
            _profilebImageUrl.postValue(result.getOrNull()) // This will be String? or null
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
    suspend fun getUserByIdUsr(userId: String): User? {
        return try {
            val result = createUserInteractor.getUserById(userId)
            result.getOrNull() // Eğer işlem başarılıysa User döner, başarısızsa null döner
        } catch (e: Exception) {
            null // Eğer bir hata olursa null döndür
        }
    }



    fun loadPosts() {

            viewModelScope.launch {
                try {
                    val followingList = userPreferences.getUser()?.following ?: listOf()

                    // Interactor'dan gelen postları dinle
                    postInteractor.getPosts(followingList) { posts ->
                        _posts.value = posts
                    }
                    hasFetchedPosts = true
                } catch (e: Exception) {
                    _posts.value = emptyList() // Hata durumunda boş liste döner
                }
            }

    }
    fun refreshPostsAfterFollow() {
        viewModelScope.launch {
            try {
                val followingList = userPreferences.getUser()?.following ?: listOf()

                // Takip edilen kullanıcıların postlarını dinlemeye başla
                postInteractor.getPosts(followingList) { posts ->
                    _posts.value = posts
                }
            } catch (e: Exception) {
                _posts.value = emptyList()
            }
        }
    }


    fun refreshGallery() {
        viewModelScope.launch {
            try {
                val followingList = userPreferences.getUser()?.following ?: listOf()

                // Postları yenile ve dinle
                postInteractor.getPosts(followingList) { posts ->
                    _posts.value = posts
                }
            } catch (e: Exception) {
                _posts.value = emptyList()
            }
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

    suspend fun getUserIdByEmail(): String? {
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
                    _postUploadStatus.value = ApiResponse.Loading()
                    val result = postInteractor.uploadPhoto(imageUri, userId)
                    if (result.isSuccess) {
                        _postUploadStatus.value = ApiResponse.Success(true)
                    }
                } else {
                    _postUploadStatus.value = ApiResponse.Fail(java.lang.Exception("\"User ID not found\""))
                }
            } catch (e: Exception) {
                _postUploadStatus.value = ApiResponse.Fail(e)
            }
        }
    }

}