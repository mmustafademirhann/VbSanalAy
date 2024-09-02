package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.MessageItemBinding
import com.example.socialmediavbsanalay.domain.model.MessageItem
import com.example.socialmediavbsanalay.domain.model.Story
import javax.inject.Inject

class MessageItemAdaptor @Inject constructor() : RecyclerView.Adapter<MessageItemAdaptor.MessageItemViewHolder>() {

    private var messageItems: List<MessageItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageItemViewHolder, position: Int) {
        holder.bind(messageItems[position])
    }


    override fun getItemCount(): Int = messageItems.size

    fun setMessageItems(newMessageItems: List<MessageItem>) {
         messageItems = newMessageItems
        notifyDataSetChanged()
    }
    fun loadMessages(): List<MessageItem> {
        return listOf(
            MessageItem(imageResId = R.drawable.aysegulmustafa, username = "user1","dfssdfsd"),
            MessageItem(imageResId = R.drawable.aysegulmustafa, username = "user1","dfssdfsd"),
            MessageItem(imageResId = R.drawable.aysegulmustafa, username = "user1","dfssdfsd"),
            MessageItem(imageResId = R.drawable.aysegulmustafa, username = "user1","dfssdfsd"),
            MessageItem(imageResId = R.drawable.aysegulmustafa, username = "user1","dfssdfsd"),
            MessageItem(imageResId = R.drawable.aysegulmustafa, username = "user1","dfssdfsd"),
            MessageItem(imageResId = R.drawable.aysegulmustafa, username = "user1","dfssdfsd"),
            MessageItem(imageResId = R.drawable.aysegulmustafa, username = "user1","dfssdfsd"),
            MessageItem(imageResId = R.drawable.aysegulmustafa, username = "user1","dfssdfsd"),




        )
    }

    class MessageItemViewHolder(private val binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messageItem: MessageItem) {
            binding.profileImage.setImageResource(messageItem.imageResId) // Example
            binding.chatName.text = messageItem.username
            binding.chatMessage.text = messageItem.chat
        }

    }
}
