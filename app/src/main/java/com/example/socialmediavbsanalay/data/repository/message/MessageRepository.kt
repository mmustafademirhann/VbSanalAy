package com.example.socialmediavbsanalay.data.repository.message

import com.example.socialmediavbsanalay.domain.model.MessageItem


interface MessageRepository {
    suspend fun getMessageItems(): Result<List<MessageItem>>
}