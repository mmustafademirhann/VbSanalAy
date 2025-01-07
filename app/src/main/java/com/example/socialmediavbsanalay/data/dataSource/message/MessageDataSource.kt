package com.example.socialmediavbsanalay.data.dataSource.message

import com.example.socialmediavbsanalay.domain.model.MessageItem

interface MessageDataSource {
    suspend fun sendMessage(chatRoomId: String, message: MessageItem)
    fun readMessages(chatRoomId: String, onMessagesReceived: (List<MessageItem>) -> Unit)
    suspend fun getMessageItems(chatRoomId: String): List<MessageItem>  // Bu fonksiyonu ekleyin
}