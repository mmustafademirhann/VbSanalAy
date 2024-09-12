package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.PostForRecyclerBinding
import com.example.socialmediavbsanalay.domain.model.Post

import javax.inject.Inject

class PostAdapter @Inject constructor() : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {


    private var posts: List<Post> = emptyList()


    class PostViewHolder(private val binding:PostForRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            // Bind views using the View Binding
            binding.postUsername.text = post.username
            binding.postContent.text = "Nasıl ?"

            // Load image using Glide
            Glide.with(binding.root)
                .load(post.imageResId) // Assuming `post.imageUrl` holds the URL of the image
                .into(binding.postImage)
        }
    }

    fun setPosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
    companion object {
        fun from(parent: ViewGroup): PostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = PostForRecyclerBinding.inflate(layoutInflater, parent, false)
            return PostViewHolder(binding)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            PostForRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }
}