package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.MessageItemBinding
import com.example.socialmediavbsanalay.domain.model.MessageItem
class MessageItemAdaptor : RecyclerView.Adapter<MessageItemAdaptor.MessageItemViewHolder>() {

    private var messageItems: List<MessageItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageItemViewHolder, position: Int) {
        holder.bind(messageItems[position])
    }

    override fun getItemCount(): Int = messageItems.size

    // Mesajları adaptöre set etmek
    fun setMessageItems(newMessageItems: List<MessageItem>) {
        messageItems = newMessageItems
        notifyDataSetChanged()
    }

    class MessageItemViewHolder(private val binding: MessageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messageItem: MessageItem) {
            binding.chatName.text = messageItem.sender?.name ?: messageItem.receiver?.name  // Kullanıcı adı
            Glide.with(binding.profileImage.context)
                .load(messageItem.sender?.profileImageUrl ?: messageItem.receiver?.profileImageUrl) // Profil resmi
                .into(binding.profileImage)  // Profil resmini gösteriyoruz
            binding.chatMessage.text = messageItem.message // Mesaj içeriği
        }
    }
}
