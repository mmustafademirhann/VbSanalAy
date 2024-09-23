package com.example.socialmediavbsanalay.presentation.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.socialmediavbsanalay.databinding.PostForRecyclerRecyclerBinding
import com.example.socialmediavbsanalay.domain.model.Post

class PostDetailAdapter : RecyclerView.Adapter<PostDetailAdapter.PostDetailViewHolder>() {

    private var posts: List<Post> = emptyList()

    class PostDetailViewHolder(private val binding: PostForRecyclerRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            // Load image using Glide
            Glide.with(binding.root)
                .load(post.imageResId) // Assuming `post.imageResId` holds the URL of the image
                .into(binding.postImageE)

            // Bind other post details
            binding.postUsernameE.text = post.username // Example of setting text
        }
    }

    fun setPosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostDetailViewHolder {
        val binding = PostForRecyclerRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostDetailViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size
}
