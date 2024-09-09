package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.user.UserDataSource
import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.user.UserRepositoryImpl
import com.example.socialmediavbsanalay.domain.interactor.user.UserInteractor
import com.example.socialmediavbsanalay.data.dataSourceImpl.user.UserDataSourceImpl

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {


    @Provides
    @Singleton
    fun provideUserDataSource(
        firebaseDatabase: FirebaseDatabase,
        firestore: FirebaseFirestore // No need to provide here if already provided in FirebaseModule
    ): UserDataSource {
        return UserDataSourceImpl(firebaseDatabase, firestore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseUserDataSource: UserDataSource
    ): UserRepository {
        return UserRepositoryImpl(firebaseUserDataSource)
    }

    @Provides
    @Singleton
    fun provideUserInteractor(
        userRepository: UserRepository
    ): UserInteractor {
        return UserInteractor(userRepository)
    }
}