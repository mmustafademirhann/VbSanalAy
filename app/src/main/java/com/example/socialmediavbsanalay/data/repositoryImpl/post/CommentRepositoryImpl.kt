package com.example.socialmediavbsanalay.data.repositoryImpl.post

import android.content.ContentValues.TAG
import android.util.Log
import com.example.socialmediavbsanalay.data.repository.post.CommentRepository
import com.example.socialmediavbsanalay.domain.model.Comment
import com.example.socialmediavbsanalay.data.dataSource.post.CommentDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(private val commentDataSource: CommentDataSource) : CommentRepository {

    override suspend fun addComment(comment: Comment): Result<Unit> {
        return commentDataSource.addComment(comment) // Yorum ekle
    }

    override fun getComments(postId: String): Flow<List<Comment>> {
        return commentDataSource.getComments(postId) // Yorumları al
    }
    override fun getCommentsForPost(postId: String, onCommentsUpdated: (List<Comment>) -> Unit) {
        commentDataSource.fetchCommentsForPost(postId) { comments ->
            // Call the callback with the list of comments from the data source
            onCommentsUpdated(comments)
        }
    }
}
