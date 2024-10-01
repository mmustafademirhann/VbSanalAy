package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.PostForRecyclerBinding
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel

import javax.inject.Inject

class PostAdapter @Inject constructor(
    private val userViewModel: UserViewModel ,
    private val onCommentClick: (String) -> Unit// UserViewModel'i enjekte et
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private val mockItem=Post("","","mock",null)

    private var posts: List<Post> = emptyList()

    class PostViewHolder( val binding: PostForRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, users: List<User>?,onCommentClick: (String) -> Unit) {
            binding.postUsername.text = post.username
            binding.postContent.text = "Nasıl ?"


            // Find the correct user profile based on the post's username
            val matchingUser = users?.find { user -> user.id == post.username }

            if (matchingUser != null) {
                // Load the user's profile image with Glide using circle crop
                Glide.with(binding.root)
                    .load(matchingUser.profileImageUrl) // URL of the user's profile image
                    .circleCrop() // This will transform the image into a circular shape// Optional error image
                    .into(binding.storyImageView) // Ensure `storyImageView` is correctly referenced
            } else {
                // Load a default profile image with circle crop
                Glide.with(binding.root)
                    .load(R.drawable.shin) // Default profile image
                    .circleCrop() // Transform the image into a circular shape
                    .into(binding.storyImageView)
            }
            binding.commentImage.setOnClickListener {
                onCommentClick(post.id) // Burada post'un ID'sini geçiriyoruz
            }
            // Load the post image using Glide

        }

    }
    companion object {
        fun from(parent: ViewGroup): PostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = PostForRecyclerBinding.inflate(layoutInflater, parent, false)
            return PostViewHolder(binding)
        }
    }

    fun setPosts(newPosts: List<Post>) {
        posts = newPosts+mockItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostForRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        // Kullanıcı bilgilerini al ve gözlemle
        userViewModel.getAllUsers() // Öncelikle kullanıcıyı çek



        userViewModel.usersListt.observeForever { result -> // LiveData'yı gözlemle
            val profileImageUrl =
                result?.getOrNull() // Keskulla profil resmini al
            holder.bind(post, profileImageUrl,onCommentClick) // Post ve profil resmini bağla
        }


        Glide.with(holder.itemView.context)
            .load(post.imageResId)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image
            .dontAnimate() // Optional: Skip the fade animation if it's causing issues
            .placeholder(null) // No placeholder
            .error(R.drawable.sayfabitti)
            .into(holder.binding.postImage)
    }
}
