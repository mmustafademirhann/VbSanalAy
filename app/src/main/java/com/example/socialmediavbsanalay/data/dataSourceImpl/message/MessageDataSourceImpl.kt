package com.example.socialmediavbsanalay.data.dataSourceImpl.message

import com.example.socialmediavbsanalay.data.dataSource.message.MessageDataSource
import com.example.socialmediavbsanalay.domain.model.MessageItem
import javax.inject.Inject

class MessageDataSourceImpl @Inject constructor(

): MessageDataSource {
    override suspend fun fetchMessageItems(): Result<List<MessageItem>> {
        return Result.success(emptyList())
    }
}