package com.example.socialmediavbsanalay.data.repository.message

import com.example.socialmediavbsanalay.domain.model.MessageItem

interface MessageRepository {
    suspend fun sendMessage(chatRoomId: String, message: MessageItem)
    fun readMessages(chatRoomId: String, onMessagesReceived: (List<MessageItem>) -> Unit)
    suspend fun getMessageItems(chatRoomId: String): List<MessageItem> // This should return List<MessageItem>
}