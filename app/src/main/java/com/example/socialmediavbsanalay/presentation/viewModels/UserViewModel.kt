package com.example.socialmediavbsanalay.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.domain.interactor.post.GetUserStoriesUseCase
import com.example.socialmediavbsanalay.domain.interactor.post.PostInteractor
import com.example.socialmediavbsanalay.domain.interactor.post.StoryInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.CreateUserInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.UserInteractor
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.domain.model.UserStories
import com.example.socialmediavbsanalay.presentation.viewModels.utils.SingleLiveData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await


import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val createUserInteractor: CreateUserInteractor,
    private val postInteractor: PostInteractor,
    private val storyInteractor: StoryInteractor,
    private val getUserStoriesUseCase: GetUserStoriesUseCase
) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> get() = _userData

    private val _usersList = MutableLiveData<List<User>>()
    val usersList: LiveData<List<User>> get() = _usersList

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

    private val _currentUser = MutableLiveData<Result<User?>>()
    val currentUser: LiveData<Result<User?>> = _currentUser

    private val _usersListt = MutableLiveData<Result<List<User>>?>()
    val usersListt: LiveData<Result<List<User>>?> = _usersListt

    private val _likeSuccess = SingleLiveData<Boolean>()
    val likeSuccess: SingleLiveData<Boolean> get() = _likeSuccess

    private val _likeError = MutableLiveData<String>()
    val likeError: LiveData<String> get() = _likeError

    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> get() = _stories


    private val _userStories = MutableLiveData<ApiResponse<ArrayList<UserStories>>>()
    val userStories: LiveData<ApiResponse<ArrayList<UserStories>>> get() = _userStories
    @Inject
    lateinit var userPreferences: UserPreferences

    private val _usersWithStories = MutableLiveData<List<User>>()
    val usersWithStories: LiveData<List<User>> get() = _usersWithStories
    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> get() = _isFollowing

    private val _followerCount = MutableLiveData<Int>()
    val followerCount: LiveData<Int> get() = _followerCount
    private val _followingCount = MutableLiveData<Int>()
    val followingCount: LiveData<Int> get() = _followingCount

    private val _followers = MutableLiveData<List<User>>() // List<User> türünde LiveData
    val followers: LiveData<List<User>> get() = _followers // Public getter


    private val _following = MutableLiveData<List<User>>() // Takip edilen ID'ler
    val following: LiveData<List<User>> get() = _following



    // Kullanıcıyı takip ediyor mu kontrolü

    fun loadFollowers(userId: String) {
        viewModelScope.launch {
            val userDoc = userInteractor.getUserDocument(userId)
            if (userDoc != null) {
                val user = userDoc.toObject(User::class.java)

                // Takipçi ID'lerini al
                Log.d("UserFollowersFragment", "User: $user")
                val followerIds = user?.followers ?: emptyList()
                Log.d("UserFollowersFragment", "Follower IDs: $followerIds")
                // Kullanıcıları yüklemek için bir liste oluştur
                val followerUsers = mutableListOf<User>()

                // Her bir takipçi ID'si için User nesnesini yükle
                for (followerId in followerIds) {
                    val followerDoc = userInteractor.getUserDocument(followerId)
                    val followerUser = followerDoc?.toObject(User::class.java)
                    if (followerUser != null) {
                        followerUsers.add(followerUser)
                    }
                }

                _followers.value = followerUsers // LiveData'ya takipçi kullanıcılarını at
            } else {
                _followers.value = emptyList() // Kullanıcı bulunamazsa boş liste
            }
        }
    }


    fun loadFollowing(userId: String) {
        viewModelScope.launch {
            val userDoc = userInteractor.getUserDocument(userId)
            if (userDoc != null) {
                val user = userDoc.toObject(User::class.java)

                // Takipçi ID'lerini al
                Log.d("UserFollowersFragment", "User: $user")
                val followerIds = user?.following ?: emptyList()
                Log.d("UserFollowersFragment", "Follower IDs: $followerIds")
                // Kullanıcıları yüklemek için bir liste oluştur
                val followingUsers = mutableListOf<User>()

                // Her bir takipçi ID'si için User nesnesini yükle
                for (followerId in followerIds) {
                    val followingDoc = userInteractor.getUserDocument(followerId)
                    val followingUser = followingDoc?.toObject(User::class.java)
                    if (followingUser != null) {
                        followingUsers.add(followingUser)
                    }
                }

                _following.value = followingUsers // LiveData'ya takipçi kullanıcılarını at
            } else {
                _following.value = emptyList() // Kullanıcı bulunamazsa boş liste
            }
        }
    }
    fun checkIfUserIsFollowing(currentUserId: String, targetUserId: String) {
        viewModelScope.launch {
            val result = userInteractor.isUserFollowing(currentUserId, targetUserId)
            _isFollowing.value = result
        }
    }

    // Takipçi sayısını yükleme
    fun loadFollowerCount(userId: String) {
        viewModelScope.launch {
            val userDoc = userInteractor.getUserDocument(userId)
            if (userDoc != null) {
                // User nesnesini oluştur
                val user = userDoc.toObject(User::class.java)
                // followers listesinin boyutunu kullan
                _followerCount.value = user?.followers?.size ?: 0
            } else {
                _followerCount.value = 0
            }
        }
    }

    fun loadFollowingCount(userId: String) {
        viewModelScope.launch {
            val userDoc = userInteractor.getUserDocument(userId)
            if (userDoc != null) {
                val user = userDoc.toObject(User::class.java)
                _followingCount.value = user?.following?.size ?: 0 // followers listesinin boyutunu kullan
            } else {
                _followingCount.value = 0
            }
        }
    }


    // Takip durumunu değiştirme (takip etme/takipten çıkma)
    suspend fun toggleFollowStatus(currentUserId: String, targetUserId: String) {
        val isCurrentlyFollowing = isFollowing.value ?: false
        if (isCurrentlyFollowing) {
            // Takipten çıkma işlemi
            val success = userInteractor.unfollow(currentUserId, targetUserId)

            if (success) {
                // Takipçi sayısını güncelleme
                userInteractor.updateFollowerCount(targetUserId, false)
                _isFollowing.value = false
            }
        } else {
            // Takip etme işlemi
            val success = userInteractor.follow(currentUserId, targetUserId)

            if (success) {
                // Takipçi sayısını güncelleme
                userInteractor.updateFollowerCount(targetUserId, true)
                _isFollowing.value = true
            }
        }
        // Takipçi sayısını tekrar yükle
        loadFollowerCount(targetUserId)
        //loadFollowerCount(currentUserId)
    }
    // Logout işlemi simülasyonu







    private val _uploadStoryLiveData = MutableLiveData<ApiResponse<Boolean>>()

    val uploadStoryLiveData: LiveData<ApiResponse<Boolean>> get() = _uploadStoryLiveData
    fun loadUsersWithStories() {
        viewModelScope.launch {
            val followingList = userPreferences.getUser()?.following ?: listOf()

            if (followingList.isEmpty()) {
                Log.d("loadUsersWithStories", "Kullanıcı hiç kimseyi takip etmiyor.")
                _usersWithStories.value = emptyList() // Boş liste döndür
                return@launch
            }

            try {
                val usersList = userInteractor.getUsersWithStories(followingList)
                Log.d("UsersWithStories", "Kullanıcılar: $usersList")

                if (usersList.isNotEmpty()) {
                    _usersWithStories.value = usersList
                } else {
                    Log.d("UsersWithStories", "Takip edilen kullanıcıların hiçbiri hikaye paylaşmamış.")
                    _usersWithStories.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("loadUsersWithStories", "Hata: ${e.message}")
                _usersWithStories.value = emptyList()
            }
        }
    }


    fun fetchUserStories() {
        viewModelScope.launch {
            _userStories.value = ApiResponse.Loading()
            try {
                val followingList = userPreferences.getUser()?.following ?: listOf()
                val stories = getUserStoriesUseCase(followingList)
                _userStories.value = ApiResponse.Success(stories)
            } catch (e: Exception) {
                // Hata işleme
                Log.e("StoryViewModel", "Error fetching stories: ${e.message}")
                _userStories.value = ApiResponse.Fail(e)
            }
        }
    }

    fun updateSeenStatusOfStory(story: Story?, currentUserId: String) {
        viewModelScope.launch {
            try {
                storyInteractor.updateSeenStatusOfStory(story, currentUserId)
            } catch (e: Exception) {
                Log.e("StoryViewModel", "Error update seen status: ${e.message}")
            }
        }
    }
    fun fetchAllUsersExcludingCurrentUser() {
        viewModelScope.launch {
            try {
                val allUsers = userInteractor.getAllUsers() // Tüm kullanıcıları al
                val currentUserId = userPreferences.getUser()?.id.toString()

                // Giriş yapmış kullanıcının dışında kalan kullanıcıları filtrele
                val filteredUsers = allUsers.filter { it.id != currentUserId }

                _usersList.value = filteredUsers // Filtrelenmiş kullanıcı listesini güncelle
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error fetching users: ${e.message}")
            }
        }
    }




    suspend fun getAllStories(): List<Story> {
        return try {
            val storiesList = mutableListOf<Story>()
            val snapshot = firestore.collection("stories").get().await()

            for (document in snapshot.documents) {
                val story = document.toObject(Story::class.java)
                if (story != null) {
                    storiesList.add(story)
                }
            }
            storiesList
        } catch (e: Exception) {
            emptyList() // Hata durumunda boş liste döndür
        }
    }

    fun fetchStories() {
        viewModelScope.launch {
            // Fetch the stories asynchronously
            val response = storyInteractor.fetchStories()
            if (response.isSuccess) {
                _stories.postValue(response.getOrNull() ?: emptyList()) // Update LiveData with the fetched stories
            } else {
                // Handle error (e.g., log it or update with an empty list)
                _stories.postValue(emptyList()) // Ensure LiveData is updated
            }
        }
    }


    fun uploadStory(story: Story) {
        viewModelScope.launch {
            _uploadStoryLiveData.value = ApiResponse.Loading()
            val response = storyInteractor.uploadStory(story)
            if (response.isSuccess) {
                _uploadStoryLiveData.postValue(ApiResponse.Success(true))
            } else {
                _uploadStoryLiveData.postValue(ApiResponse.Fail(java.lang.Exception()))
            }
        }
    }


    fun likeOrUnLikePost(postId: String, userId: String, isLike: Boolean) {
        postInteractor.likeOrUnlikePost(postId, userId, isLike,
            onSuccess = {
                // Başarılı olduğunda UI'yı güncelle
                _likeSuccess.value = isLike
            },
            onFailure = { e ->
                _likeError.value = e.message // Hata olduğunda UI'yı güncelle
            }
        )
    }
    fun searchUsers(query: String) {
        viewModelScope.launch {
            userInteractor.searchUsers(query).collect { userList ->
                _users.value = userList
            }
        }
    }


    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                userInteractor.addUser(user)
                _userData.value = user
                // Notify UI of success
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }
    fun fetchCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = createUserInteractor.getCurrentUser()
        }
    }
    fun getAllUsers() {
        viewModelScope.launch {
            try {
                val usersResultt = createUserInteractor.getAllUser()
                _usersListt.value = usersResultt
            } catch (e: Exception) {
                _usersListt.value = Result.failure(e)
            }
        }
    }
    fun fetchUser(userId: String) {
        viewModelScope.launch {
            try {
                val user = userInteractor.getUser(userId)
                _userData.value = user
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    fun fetchAllUsers() {
        viewModelScope.launch {
            try {
                val users = userInteractor.getAllUsers()
                _usersList.value = users
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }
}
