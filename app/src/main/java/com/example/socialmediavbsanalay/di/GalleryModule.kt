package com.example.socialmediavbsanalay.di

import android.app.Application
import android.content.Context
import com.example.socialmediavbsanalay.data.dataSource.gallery.GalleryDataSource
import com.example.socialmediavbsanalay.data.dataSourceImpl.gallery.GalleryDataSourceImpl
import com.example.socialmediavbsanalay.data.repository.gallery.GalleryRepository
import com.example.socialmediavbsanalay.data.repositoryImpl.gallery.GalleryRepositoryImpl
import com.example.socialmediavbsanalay.domain.interactor.gallery.GalleryInteractor
import com.example.socialmediavbsanalay.domain.interactor.gallery.GalleryInteractorImpl
import com.example.socialmediavbsanalay.presentation.adapters.GalleryAdapter
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GalleryModule {

    @Provides
    fun provideGalleryAdapter(): GalleryAdapter {
        return GalleryAdapter()
    }

    @Provides
    @Singleton
    fun provideGalleryDataSource(@ApplicationContext context: Context,firebaseStorage: FirebaseStorage): GalleryDataSource {
        return GalleryDataSourceImpl(context,firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideGalleryRepository(dataSource: GalleryDataSource): GalleryRepository {
        return GalleryRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideGalleryInteractor(repository: GalleryRepository): GalleryInteractor {
        return GalleryInteractorImpl(repository)
    }
}
