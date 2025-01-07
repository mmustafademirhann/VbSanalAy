package com.example.socialmediavbsanalay.domain.model

data class MessageItem(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "sent",  // "sent", "failed", "delivered"
    var sender: User? = null, // Mesajın gönderen kullanıcısı
    var receiver: User? = null // Mesajın alıcı kullanıcısı
)
