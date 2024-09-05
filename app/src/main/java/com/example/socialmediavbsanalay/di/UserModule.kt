package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.user.UserDataSource
import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.user.UserRepositoryImpl
import com.example.socialmediavbsanalay.domain.interactor.user.UserInteractor
import com.example.socialmediavbsanalay.data.dataSourceImpl.user.UserDataSourceImpl

import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserModule {


    @Provides
    fun provideFirebaseUserDataSource(
        database: FirebaseDatabase
    ): UserDataSource {
        return UserDataSourceImpl(database)
    }

    @Provides
    fun provideUserRepository(
        firebaseUserDataSource: UserDataSource
    ): UserRepository {
        return UserRepositoryImpl(firebaseUserDataSource)
    }

    @Provides
    fun provideUserInteractor(
        userRepository: UserRepository
    ): UserInteractor {
        return UserInteractor(userRepository)
    }
}