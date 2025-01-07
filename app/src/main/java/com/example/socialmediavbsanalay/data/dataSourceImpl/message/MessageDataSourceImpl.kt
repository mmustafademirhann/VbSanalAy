package com.example.socialmediavbsanalay.data.dataSourceImpl.message

import android.util.Log
import com.example.socialmediavbsanalay.data.dataSource.message.MessageDataSource
import com.example.socialmediavbsanalay.domain.model.MessageItem
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessageDataSourceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : MessageDataSource {

    override suspend fun sendMessage(chatRoomId: String, message: MessageItem) {
        val messageRef = firebaseFirestore.collection("messages")
            .document(chatRoomId) // Get the specific chat room document
            .collection("chat") // Sub-collection for messages

        val newMessage = message.copy(timestamp = System.currentTimeMillis())  // Add timestamp
        messageRef.add(newMessage)  // Send the message
            .addOnSuccessListener {
                Log.d("Message", "Message sent successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Message", "Error sending message", e)
            }
    }

    override fun readMessages(chatRoomId: String, onMessagesReceived: (List<MessageItem>) -> Unit) {
        val messagesRef = firebaseFirestore.collection("messages")
            .document(chatRoomId) // Get the specific chat room document
            .collection("chat") // Sub-collection for messages

        // Listen for messages in the chat room
        messagesRef.orderBy("timestamp")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("Message", "Error getting messages", exception)
                    return@addSnapshotListener
                }

                val messages = mutableListOf<MessageItem>()
                snapshot?.documents?.forEach { doc ->
                    val message = doc.toObject(MessageItem::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }
                onMessagesReceived(messages)  // Return the loaded messages
            }
    }
    override suspend fun getMessageItems(chatRoomId: String): List<MessageItem> {
        val messages = mutableListOf<MessageItem>()

        // Get messages from Firestore
        val snapshot = firebaseFirestore.collection("messages")
            .document(chatRoomId) // chatRoomId'yi kullanarak mesajları alıyoruz
            .collection("chat")
            .orderBy("timestamp")
            .get()
            .await()

        for (document in snapshot.documents) {
            val message = document.toObject(MessageItem::class.java) // Convert Firestore document to MessageItem
            message?.let {
                // After getting the message, get sender and receiver info
                val sender = firebaseFirestore.collection("user").document(message.senderId)
                    .get().await().toObject(User::class.java)
                val receiver = firebaseFirestore.collection("user").document(message.receiverId)
                    .get().await().toObject(User::class.java)

                // Set sender and receiver objects in the message
                it.sender = sender
                it.receiver = receiver

                // Add the message to the list
                messages.add(it)
            }
        }
        return messages
    }

}
