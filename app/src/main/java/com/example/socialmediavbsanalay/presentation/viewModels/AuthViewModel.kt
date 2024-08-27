package com.example.socialmediavbsanalay.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.domain.interactor.authentication.AuthInteractor
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {

    private val _authState = MutableLiveData<Result<FirebaseUser?>>()
    val authState: LiveData<Result<FirebaseUser?>> get() = _authState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            //viewmodel içerisinde concurrent işlemleri yapmal için kullanılır.
            _authState.value = authInteractor.signIn(email, password)
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = authInteractor.signUp(email, password)
        }
    }
}