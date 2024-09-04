package com.example.socialmediavbsanalay.data.repositoryImpl.message

import com.example.socialmediavbsanalay.data.dataSource.message.MessageDataSource
import com.example.socialmediavbsanalay.data.repository.message.MessageRepository
import com.example.socialmediavbsanalay.domain.model.MessageItem
import com.example.socialmediavbsanalay.domain.model.Post
import javax.inject.Inject

class MessageItemRepositoryImpl @Inject constructor(
    private val messageItemDataSource: MessageDataSource
):MessageRepository {


    override suspend fun getMessageItems(): Result<List<MessageItem>> {
        return messageItemDataSource.fetchMessageItems()
    }
}