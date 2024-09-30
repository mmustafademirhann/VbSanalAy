package com.example.socialmediavbsanalay.domain.interactor.messageItem

import com.example.socialmediavbsanalay.data.repository.message.MessageRepository
import com.example.socialmediavbsanalay.domain.model.MessageItem
import javax.inject.Inject

class MessageItemInteractor @Inject constructor(
    private val messageItemRepository: MessageRepository
) {

    suspend fun getMessageItems(): Result<List<MessageItem>> {
        return messageItemRepository.getMessageItems()
    }
}