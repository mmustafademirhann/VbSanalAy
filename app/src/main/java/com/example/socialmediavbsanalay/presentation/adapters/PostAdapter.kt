package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.PostForRecyclerBinding
import com.example.socialmediavbsanalay.domain.model.Post

import javax.inject.Inject

class PostAdapter @Inject constructor() : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {


    private var post: List<Post> = emptyList()


    class PostViewHolder(private val binding: PostForRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.postImage.setImageResource(post.imageResId) // Example
            binding.postUsername.text = post.username
        }
    }

    fun setPosts(newPosts: List<Post>) {
        post = newPosts
        notifyDataSetChanged()
    }

    fun loadPosts(): List<Post> {
        return listOf(
            //Story(imageResId = R.drawable.aysegulmustafa, username = "user1"),

            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.kaydol, username = "user1"),
            Post(imageResId = R.drawable.heart, username = "user1"),
            Post(imageResId = R.drawable.bra, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.family, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Post(imageResId = R.drawable.ic_launcher_background, username = "user1"),
            Post(imageResId = R.drawable.email, username = "user1"),
            Post(imageResId = R.drawable.ekle, username = "user1"),
            Post(imageResId = R.drawable.rainy_minecraft, username = "user1"),
            Post(imageResId = R.drawable.aysegulmustafa, username = "user1"),


            )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            PostForRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = post.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(post[position])
    }
}