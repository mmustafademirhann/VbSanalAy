package com.example.socialmediavbsanalay.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.domain.interactor.authentication.AuthInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.CreateUserInteractor
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val createUserInteractor: CreateUserInteractor
) : ViewModel() {

    private val _authState = MutableLiveData<Result<FirebaseUser?>>()
    val authState: LiveData<Result<FirebaseUser?>> get() = _authState
    private val _signUpResult = MutableLiveData<Result<FirebaseUser?>>()
    val signUpResult: LiveData<Result<FirebaseUser?>> get() = _signUpResult
    private val _createUserLiveData = MutableLiveData<Result<Unit>>()
    val createUserLiveData = MutableLiveData<Result<Unit>>()


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = authInteractor.signIn(email, password)
        }
    }



    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                _signUpResult.value = authInteractor.signUp(email, password)
            }
        }
    }

    fun createUser() {
        viewModelScope.launch {
            _createUserLiveData.value = createUserInteractor.createUser(
                "UserID",
                User(
                    "Id",
                    "Name",
                    "Email"
                )
            )
        }
    }
}