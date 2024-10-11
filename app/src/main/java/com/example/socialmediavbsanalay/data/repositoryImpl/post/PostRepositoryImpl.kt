package com.example.socialmediavbsanalay.data.repositoryImpl.post

import android.net.Uri
import com.example.socialmediavbsanalay.data.dataSource.post.PostDataSource
import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.domain.model.Post
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource
): PostRepository {
    override suspend fun getPosts(
        followingList: List<String>,
        onPostsUpdated: (List<Post>) -> Unit
    ) {
        postDataSource.getPosts(followingList, onPostsUpdated) // Data source'dan gelen veriyi geri döndür
    }
    override suspend fun uploadPhoto(imageUri: Uri,userId: String): Unit {
        return postDataSource.uploadPhoto(imageUri,userId)
    }

    override fun likePost(
        postId: String,
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        return postDataSource.likePost(postId,userId,onSuccess,onFailure)
    }
    override suspend fun getPostsByFollowingUsers(followingList: List<String>): List<Post> {
        return postDataSource.fetchFollowedUsersPosts(followingList)
    }

}