package com.example.socialmediavbsanalay.data.dataSource.message

import com.example.socialmediavbsanalay.domain.model.MessageItem
import com.example.socialmediavbsanalay.domain.model.Post

interface MessageDataSource {
    suspend fun fetchMessageItems(): Result<List<MessageItem>>
}