package com.example.socialmediavbsanalay.domain.interactor.messageItem

import com.example.socialmediavbsanalay.data.repository.message.MessageRepository
import com.example.socialmediavbsanalay.domain.model.MessageItem
import javax.inject.Inject

class MessageItemInteractor @Inject constructor(
    private val messageItemRepository: MessageRepository
) {

    suspend fun getMessageItems(chatRoomId: String): List<MessageItem> {
        return messageItemRepository.getMessageItems(chatRoomId) // Firebase'den mesajları al
    }
    suspend fun sendMessage(chatRoomId: String, message: MessageItem) {
        messageItemRepository.sendMessage(chatRoomId, message) // Send message using repository
    }
}
