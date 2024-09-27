package com.example.socialmediavbsanalay.domain.interactor.authentication

import com.example.socialmediavbsanalay.data.repository.authentication.AuthRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.authentication.AuthRepositoryImpl
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun signIn(email: String, password: String): Result<FirebaseUser?> {
        return authRepository.signIn(email, password)
    }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser?> {
        return authRepository.signUp(email, password)
    }
    suspend fun getUserIdByEmail(email: String):String{
        return authRepository.getUserIdByEmail(email)

    }

    suspend fun getUserByEmail(email: String): Result<User?> {
        return authRepository.getUserByEmail(email)
    }
    fun getCurrentUserEmail():String{
        return authRepository.getCurrentUserEmail()
    }
    fun getCurrentUserId():String?{
        return authRepository.getCurrentUserId()
    }
    fun fetchUserIdByAuthId(authId: String): Flow<String?> = flow {
        val userIdFlow = authRepository.fetchUserIdByAuthId(authId)

        userIdFlow.collect { userId ->
            emit(userId) // Kullanıcı ID'sini emit et
        }

    }
    fun signOut() {
        authRepository.signOut()
    }
    suspend fun getUserProfileImageByEmail(email: String): Result<String?>{
       return authRepository.getUserProfileImageByEmail(email)
    }


}