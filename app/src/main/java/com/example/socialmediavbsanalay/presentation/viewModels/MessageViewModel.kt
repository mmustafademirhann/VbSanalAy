package com.example.socialmediavbsanalay.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.data.repository.message.MessageRepository
import com.example.socialmediavbsanalay.domain.interactor.messageItem.MessageItemInteractor
import com.example.socialmediavbsanalay.domain.model.MessageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageItemInteractor: MessageItemInteractor,
    private val messageItemRepository: MessageRepository // Interactor'ü inject ettik
) : ViewModel() {

    private val _messages = MutableLiveData<List<MessageItem>>()
    val messages: LiveData<List<MessageItem>> = _messages

    // Mesajları yüklemek için Interactor'den alıyoruz
    fun loadMessages(chatRoomId: String) {
        viewModelScope.launch {
            try {
                val messages = messageItemInteractor.getMessageItems(chatRoomId)
                _messages.postValue(messages)
            } catch (e: Exception) {
                Log.e("MessageViewModel", "Error loading messages", e)
            }
        }
    }

    // Mesaj göndermek için Interactor'ü kullanıyoruz
    fun sendMessage(chatRoomId: String, message: MessageItem) {
        viewModelScope.launch {
            try {
                messageItemInteractor.sendMessage(chatRoomId, message)
            } catch (e: Exception) {
                Log.e("MessageViewModel", "Error sending message", e)
            }
        }
    }

    fun listenToMessages(chatRoomId: String) {
        messageItemRepository.readMessages(chatRoomId) { newMessages ->
            _messages.postValue(newMessages) // Anlık gelen mesajları UI'a aktar
        }
    }
}
