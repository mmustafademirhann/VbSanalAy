package com.example.socialmediavbsanalay.data.dataSource.post

import com.example.socialmediavbsanalay.domain.model.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface CommentDataSource {
    suspend fun addComment(comment: Comment) : Result<Unit>
    fun getComments(postId: String): Flow<List<Comment>>
    fun fetchCommentsForPost(postId: String, onCommentsUpdated: (List<Comment>) -> Unit)
}