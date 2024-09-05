package com.example.socialmediavbsanalay.presentation.viewModels

import android.util.Printer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.domain.interactor.authentication.AuthInteractor
import com.example.socialmediavbsanalay.domain.interactor.user.UserInteractor
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val auth: FirebaseAuth,
    private val userInteractor: UserInteractor

) : ViewModel() {

    private val _authState = MutableLiveData<Result<FirebaseUser?>>()
    val authState: LiveData<Result<FirebaseUser?>> get() = _authState
    private val _signUpResult = MutableLiveData<Result<FirebaseUser?>>()
    val signUpResult: LiveData<Result<FirebaseUser?>> get() = _signUpResult




    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = authInteractor.signIn(email, password)
        }
    }



    fun signUp(email: String, password: String, name: String, surname: String, gender: String) {
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user
                firebaseUser?.let { user ->
                    // Create a new user data object
                    val newUser = User(
                        id = user.uid,
                        name = name,
                        surName = surname,
                        email = email,
                        gender = gender
                    )
                    // Store user data in Firebase
                    userInteractor.addUser(newUser)
                }
                _signUpResult.value = Result.success(firebaseUser)
            } catch (e: Exception) {
                _signUpResult.value = Result.failure(e)
            }
        }
    }


}