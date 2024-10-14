package com.example.socialmediavbsanalay.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.domain.interactor.notification.NotificationInteractor
import com.example.socialmediavbsanalay.domain.model.Gallery
import com.example.socialmediavbsanalay.domain.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationInteractor: NotificationInteractor
): ViewModel() {

    private val _addNotificationLiveData = MutableLiveData<ApiResponse<Boolean>>()
    val addNotificationLiveData: MutableLiveData<ApiResponse<Boolean>> = _addNotificationLiveData

    private val _getNotificationLiveData = MutableLiveData<ApiResponse<List<Notification>>>()
    val getNotificationLiveData: MutableLiveData<ApiResponse<List<Notification>>> = _getNotificationLiveData

    fun addNotification(notification: Notification) {
        viewModelScope.launch {
            val result = notificationInteractor.addNotification(notification)
            if (result.isSuccess) {
                _addNotificationLiveData.value = ApiResponse.Success(true)
            } else if (result.isFailure) {
                _addNotificationLiveData.value = ApiResponse.Fail(Exception(result.exceptionOrNull()))
            }
        }
    }

    fun getNotifications(currentUserId: String) {
        viewModelScope.launch {
            _getNotificationLiveData.value = ApiResponse.Loading()
            _getNotificationLiveData.value = notificationInteractor.getNotifications(currentUserId)
        }
    }
}