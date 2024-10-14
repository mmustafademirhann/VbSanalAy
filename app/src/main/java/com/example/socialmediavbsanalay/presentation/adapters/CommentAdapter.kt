package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.CommentItemBinding

import com.example.socialmediavbsanalay.domain.model.Comment
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import javax.inject.Inject

class CommentAdapter@Inject constructor(
    private val userViewModel: UserViewModel,
) : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {


    class CommentViewHolder(private val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment,users: List<User>?) {
            val matchingUser = users?.find { user -> user.id == comment.userId }
            binding.userNameTextView.text= comment.username
            binding.commentTextView.text = comment.comment
            //binding.commmentUsername.text = comment.username

            // Profil resmini yükle
            Glide.with(binding.root.context)
                .load(matchingUser?.profileImageUrl)
                .error(R.drawable.addstory)
                .circleCrop()
                .into(binding.profileImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        userViewModel.getAllUsers() // Öncelikle kullanıcıyı çek

        userViewModel.usersListt.observeForever { result -> // LiveData'yı gözlemle
            val userList =
                result?.getOrNull() // Keskulla profil resmini al
            holder.bind(comment, userList) // Post ve profil resmini bağla
        }
        //holder.bind(comment)
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.postId == newItem.postId // Yorumların benzersiz ID'si
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem // İçerik karşılaştırması
        }
    }

}