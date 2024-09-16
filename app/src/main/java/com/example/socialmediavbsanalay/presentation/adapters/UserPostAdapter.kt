package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.PostForRecyclerBinding
import com.example.socialmediavbsanalay.databinding.UserItemBinding
import com.example.socialmediavbsanalay.databinding.UserProfilePostBinding
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.domain.model.User

import javax.inject.Inject

class UserPostAdapter @Inject constructor() : RecyclerView.Adapter<UserPostAdapter.UserPostViewHolder>() {


    private var posts: List<Post> = emptyList()

    class UserPostViewHolder(private val binding: UserProfilePostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {

            // Load image using Glide
            Glide.with(binding.root)
                .load(post.imageResId) // Assuming `post.imageUrl` holds the URL of the image
                .into(binding.imageView5)
        }
    }

    fun setPosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
    companion object {
        fun from(parent: ViewGroup): UserPostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = UserProfilePostBinding.inflate(layoutInflater, parent, false)
            return UserPostViewHolder(binding)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostViewHolder {
        val binding =
            UserProfilePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserPostViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: UserPostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }
}