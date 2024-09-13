package com.example.socialmediavbsanalay.data.repositoryImpl.post

import android.net.Uri
import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.domain.model.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource
): PostRepository {
    override fun getPosts(): Flow<List<Post>> {
        return postDataSource.getPosts() // Fetch the list of photo URLs
    }
    override suspend fun uploadPhoto(imageUri: Uri): Unit {
        return postDataSource.uploadPhoto(imageUri)
    }
}