package com.example.socialmediavbsanalay.data.dataSource.message

import com.example.socialmediavbsanalay.domain.model.MessageItem

interface MessageDataSource {
    suspend fun fetchMessageItems(): Result<List<MessageItem>>
}