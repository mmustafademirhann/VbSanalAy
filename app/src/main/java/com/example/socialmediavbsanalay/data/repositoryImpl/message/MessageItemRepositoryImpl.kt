package com.example.socialmediavbsanalay.data.repositoryImpl.message

import com.example.socialmediavbsanalay.data.dataSource.message.MessageDataSource
import com.example.socialmediavbsanalay.data.repository.message.MessageRepository
import com.example.socialmediavbsanalay.domain.model.MessageItem
import javax.inject.Inject

class MessageItemRepositoryImpl @Inject constructor(
    private val dataSource: MessageDataSource
) : MessageRepository {

    override suspend fun sendMessage(chatRoomId: String, message: MessageItem) {
        dataSource.sendMessage(chatRoomId, message)
    }

    override fun readMessages(chatRoomId: String, onMessagesReceived: (List<MessageItem>) -> Unit) {
        dataSource.readMessages(chatRoomId, onMessagesReceived)
    }

    override suspend fun getMessageItems(chatRoomId: String): List<MessageItem> {
        return dataSource.getMessageItems(chatRoomId)
    }


}
