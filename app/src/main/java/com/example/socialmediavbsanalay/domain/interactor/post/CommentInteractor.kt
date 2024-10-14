package com.example.socialmediavbsanalay.domain.interactor

import com.example.socialmediavbsanalay.data.repository.post.CommentRepository
import com.example.socialmediavbsanalay.domain.model.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentInteractor @Inject constructor(private val commentRepository: CommentRepository) {

    // Yorum eklemek için bir fonksiyon
    suspend fun addComment(comment: Comment): Result<Unit> {
        return commentRepository.addComment(comment) // Yorum ekleme işlemini repo üzerinden gerçekleştir
    }

     fun getComments(postId: String): Flow<List<Comment>> {
        return commentRepository.getComments(postId)
    }
    fun loadComments(postId: String, onCommentsUpdated: (List<Comment>) -> Unit) {
        commentRepository.getCommentsForPost(postId, onCommentsUpdated)
    }
}
