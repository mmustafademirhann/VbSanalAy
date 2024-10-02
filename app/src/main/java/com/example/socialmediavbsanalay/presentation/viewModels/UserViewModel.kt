package com.example.socialmediavbsanalay.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.domain.interactor.post.PostInteractor
import com.example.socialmediavbsanalay.domain.interactor.post.StoryInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.CreateUserInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.UserInteractor
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow



import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val createUserInteractor: CreateUserInteractor,
    private val postInteractor: PostInteractor,
    private val storyInteractor: StoryInteractor


) : ViewModel() {

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

    private val _likeSuccess = MutableLiveData<Boolean>()
    val likeSuccess: LiveData<Boolean> get() = _likeSuccess

    private val _likeError = MutableLiveData<String>()
    val likeError: LiveData<String> get() = _likeError




    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> get() = _stories

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
            storyInteractor.uploadStory(story)
            fetchStories()  // Yeni hikayeyi yükledikten sonra güncelle
        }
    }


    fun likePost(postId: String, userId: String) {
        postInteractor.likePost(postId, userId,
            onSuccess = {
                _likeSuccess.value = true  // Başarılı olduğunda UI'yı güncelle
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
