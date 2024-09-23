package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.PostForRecyclerBinding
import com.example.socialmediavbsanalay.databinding.UserItemBinding
import com.example.socialmediavbsanalay.databinding.UserProfilePostBinding
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.OnItemClickListener
import com.example.socialmediavbsanalay.presentation.fragments.PostDetailFragment


import javax.inject.Inject

class UserPostAdapter @Inject constructor(private val listener: OnItemClickListener) : RecyclerView.Adapter<UserPostAdapter.UserPostViewHolder>() {

    private var posts: List<Post> = emptyList()

    // ViewHolder Sınıfı
    class UserPostViewHolder(private val binding: UserProfilePostBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            // Glide ile resim yükleme
            Glide.with(binding.root)
                .load(post.imageResId)  // post.imageResId yerine imageUrl kullanıldı
                .into(binding.imageView5)
        }
    }

    // Adapter'a yeni post listesi verildiğinde güncelle
    fun setPosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostViewHolder {
        // ViewBinding kullanarak ViewHolder oluştur
        val binding = UserProfilePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserPostViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: UserPostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)

        // Tıklama olayını yönet
        holder.itemView.setOnClickListener {
            listener.onItemClicked(post)  // Tıklanan post'u listener'a gönder
        }
    }
}
