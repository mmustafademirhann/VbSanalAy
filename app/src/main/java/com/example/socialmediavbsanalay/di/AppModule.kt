package com.example.socialmediavbsanalay.di

import com.example.socialmediavbsanalay.data.dataSource.authentication.FirebaseAuthDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.authentication.FirebaseAuthDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.authentication.AuthRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.authentication.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthDataSource(
        firebaseAuth: FirebaseAuth
    ): FirebaseAuthDataSource {
        return FirebaseAuthDataSourceImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuthDataSource: FirebaseAuthDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuthDataSource)
    }
}
