package com.example.socialmediavbsanalay.data.repository.post

import com.example.socialmediavbsanalay.domain.model.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface CommentRepository {
    suspend fun addComment(comment: Comment):Result<Unit>
    fun getComments(postId: String): Flow<List<Comment>>
    fun getCommentsForPost(postId: String, onCommentsUpdated: (List<Comment>) -> Unit)
}