package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.authentication.FirebaseAuthDataSource
import com.example.socialmediavbsanalay.data.dataSource.user.CreateUserDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.authentication.FirebaseAuthDataSourceImpl
import com.example.socialmediavbsanalay.data.dataSourceImpl.user.CreateUserDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.authentication.AuthRepository
import com.example.socialmediavbsanalay.data.repository.user.CreateUserRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.authentication.AuthRepositoryImpl
import com.example.socialmediavbsanalay.data.repositoryImpl.user.CreateUserRepositoryImpl
import com.example.socialmediavbsanalay.domain.interactor.user.CreateUserInteractor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthDataSource(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseAuthDataSource {
        return FirebaseAuthDataSourceImpl(firebaseAuth,firestore)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuthDataSource: FirebaseAuthDataSource,
        createUserDataSource: CreateUserDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuthDataSource,createUserDataSource)
    }

    @Provides
    @Singleton
    fun provideCreateUserInteractor(
        createUserRepository: CreateUserRepository
    ): CreateUserInteractor {
        return CreateUserInteractor(createUserRepository)
    }

    @Provides
    @Singleton
    fun provideCreateUserRepository(
        createUserDataSource: CreateUserDataSource
    ): CreateUserRepository {
        return CreateUserRepositoryImpl(createUserDataSource)
    }

    @Provides
    @Singleton
    fun provideCreateUserDataSource(
        firestore: FirebaseFirestore
    ): CreateUserDataSource {
        return CreateUserDataSourceImpl(firestore)
    }
}