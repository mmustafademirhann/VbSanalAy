package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediavbsanalay.databinding.UserItemBinding
import com.example.socialmediavbsanalay.domain.model.User
import javax.inject.Inject

class UserAdapter @Inject constructor(
    private val onItemClick: (String) -> Unit // Click listener parameter
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users: List<User> = listOf()


    // Function to update the users list with DiffUtil
    fun updateUsers(newUsers: List<User>) {
        val diffCallback = UserDiffCallback(users, newUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        users = newUsers
        diffResult.dispatchUpdatesTo(this)
    }

    // ViewHolder class for binding user items
    inner class UserViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.userName.text = user.id // Kullanıcı ismini gösteriyoruz


            // Tıklama olayını burada yönetiyoruz
             binding.root.setOnClickListener {
                val isim =binding.userName.text.toString()
                onItemClick(user.id) // Tıklanan kullanıcıyı döndürüyoruz
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount() = users.size

    // Inner class to handle DiffUtil logic
    private class UserDiffCallback(
        private val oldList: List<User>,
        private val newList: List<User>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
