package com.example.socialmediavbsanalay.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.data.repositoryImpl.user.UserRepositoryImpl
import com.example.socialmediavbsanalay.domain.interactor.user.CreateUserInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.UserInteractor
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow



import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userInteractor: UserInteractor


) : ViewModel() {

    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> get() = _userData

    private val _usersList = MutableLiveData<List<User>>()
    val usersList: LiveData<List<User>> get() = _usersList

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users


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
