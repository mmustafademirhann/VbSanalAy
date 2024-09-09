package com.example.socialmediavbsanalay.presentation.viewModels

import android.util.Printer
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val createUserInteractor: CreateUserInteractor

) : ViewModel() {
    val isItAuth=false
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
                _signUpResult.value = authInteractor.signUp(email, password)
            }
    }

    fun signUpAndCreateUser(email: String, password: String, name: String, surName: String, gender: String) {
        viewModelScope.launch {
            val signUpResult = authInteractor.signUp(email, password)
            _signUpResult.value = signUpResult

            // If signUp was successful, create the user profile in the database
            if (signUpResult.isSuccess) {
                val userExist = createUserInteractor.checkIfUserExists(email)
                if (!userExist) {
                    val createResult = createUserInteractor.createUser(
                        email.substringBefore("@"), // Use email prefix as user ID
                        User(email.substringBefore("@"), name, surName, email, gender) // Assuming gender is an Int
                    )
                    _createUserLiveData.value = createResult
                } else {
                    _createUserLiveData.value = Result.failure(Exception("User already exists"))
                }
            } else {
                _createUserLiveData.value = Result.failure(Exception("Sign-up failed"))
            }
        }
    }


    fun createUser(name:String,surName:String,email:String,gender:String) {
        viewModelScope.launch {
            val userExist=createUserInteractor.checkIfUserExists(email)
            if (!userExist) {
                val createResult = createUserInteractor.createUser(
                    email.substringBefore("@"), // Use email prefix as user ID
                    User(email.substringBefore("@"), name,surName,email,gender)
                )
                _createUserLiveData.value = createResult
            } else {
                _createUserLiveData.value = Result.failure(Exception("User already exists"))
            }



        }
    }

}