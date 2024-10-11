package com.example.socialmediavbsanalay.domain.interactor.post

import android.net.Uri
import com.example.socialmediavbsanalay.data.repository.post.PostRepository
import com.example.socialmediavbsanalay.domain.model.Post
import javax.inject.Inject

class PostInteractor @Inject constructor(
    private val postRepository: PostRepository
){
    suspend fun getPosts(
        followingList: List<String>,
        onPostsUpdated: (List<Post>) -> Unit
    ) {
        postRepository.getPosts(followingList, onPostsUpdated) // Repository'den veri al ve geri döndür
    }
    suspend fun uploadPhoto(imageUri: Uri,userId: String): Unit {
        return postRepository.uploadPhoto(imageUri,userId)
    }
    fun likePost(postId: String, userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        return postRepository.likePost(postId,userId,onSuccess,onFailure)
    }
    suspend fun executePost(followingList: List<String>): List<Post> {
        // Repository'den takip edilen kullanıcıların postlarını getir
        return postRepository.getPostsByFollowingUsers(followingList)
    }

}
