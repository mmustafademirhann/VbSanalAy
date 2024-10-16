package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentNotificationBarBinding
import com.example.socialmediavbsanalay.databinding.NotificationItemBinding
import com.example.socialmediavbsanalay.domain.model.Notification
import com.example.socialmediavbsanalay.domain.model.NotificationType
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel

class NotificationsAdapter(
    private val userViewModel: UserViewModel,
): RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    private var notifications: List<Notification> = emptyList()

    fun setNotifications(notifications: List<Notification>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }

    inner class NotificationsViewHolder(val binding: NotificationItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification,users: List<User>?) {
            val matchingUser = users?.find { user -> user.id == notification.username }
            Glide.with(binding.root)
                .load(matchingUser?.profileImageUrl)
                .error(R.drawable.shin)// URL of the user's profile image
                .circleCrop() // This will transform the image into a circular shape// Optional error image
                .into(binding.ivUserImage) // Ensure `storyImageView` is correctly referenced

            when(notification.notificationType) {
                NotificationType.LIKE.notificationType -> {
                    binding.tvNotification.text = "${notification.username} liked your post"
                }
                NotificationType.COMMENT.notificationType -> {
                    binding.tvNotification.text = "${notification.username} commented on your post"
                }
                NotificationType.FOLLOW.notificationType -> {
                    binding.tvNotification.text = "${notification.username} started to follow you"
                }
            }

            if (notification.postImage.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(notification.postImage)
                    .error(R.drawable.shin)// URL of the user's profile image
                    .into(binding.ivPostImage) // Ensure `storyImageView` is correctly referenced
            } else {
                binding.ivPostImage.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val notification = notifications[position]
        userViewModel.getAllUsers() // Öncelikle kullanıcıyı çek

        userViewModel.usersListt.observeForever { result -> // LiveData'yı gözlemle
            val userList =
                result?.getOrNull() // Keskulla profil resmini al
            holder.bind(notification, userList) // Post ve profil resmini bağla
        }
        //holder.bind(notification)
    }
}