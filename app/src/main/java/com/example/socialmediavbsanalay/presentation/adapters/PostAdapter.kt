package com.example.socialmediavbsanalay.presentation.adapters

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.PostForRecyclerBinding
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel

import javax.inject.Inject

class PostAdapter(
    private val currentUserId: String,
    private val userViewModel: UserViewModel,
    private val onCommentClick: (String, String, String) -> Unit,
    private val onLikeClick: (String, String, String) -> Unit,
    private val onUnLikeClick: (String, String) -> Unit,
    private val onUsernameClick: (String) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var posts: List<Post> = emptyList()

    @SuppressLint("ClickableViewAccessibility")
    inner class PostViewHolder(val binding: PostForRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, users: List<User>?) {
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
            binding.postUsername.text = post.username
            binding.postContent.text = "Nasıl ?"
            // Load the user's profile image with Glide using circle crop
            binding.commentImage.setOnClickListener {
                onCommentClick(post.id, post.username, post.imageResId) // Burada post'un ID'sini geçiriyoruz
            }
            binding.likeCount.text = post.likesCount.toString()
            binding.commentCount.text = post.commentsCount.toString()
            if (post.likedBy.contains(currentUserId)) {
                binding.unlikeImage.visibility = View.VISIBLE
                binding.likeImage.visibility = View.GONE
            } else {
                binding.unlikeImage.visibility = View.GONE
                binding.likeImage.visibility = View.VISIBLE
            }
            binding.likeImage.setOnClickListener {
                // Kullanıcı daha önce beğenmiş mi kontrol et
                if (!post.likedBy.contains(currentUserId)) {
                    // Kullanıcı daha önce beğenmemişse, işlemi gerçekleştirelim
                    post.likedBy.add(currentUserId)
                    post.likesCount++

                    // Görünümde beğenme durumu güncelleniyor
                    binding.likeCount.text = post.likesCount.toString()
                    binding.likeImage.visibility = View.GONE
                    binding.unlikeImage.visibility = View.VISIBLE

                    // Veritabanına işlemi kaydedelim (Firestore'a da kaydedelim)
                    userViewModel.likeOrUnLikePost(post.id, currentUserId, true)

                    // Adapter'ı güncelle
                    val position = getPositionForPost(post.id)
                    if (position != -1) {
                        notifyItemChanged(position)
                    }
                } else {
                    // Eğer kullanıcı daha önce beğenmişse, beğenme işlemi yapılmasın
                    //Toast.makeText(requireContext(), "Bu postu zaten beğendiniz!", Toast.LENGTH_SHORT).show()
                }
            }
            binding.unlikeImage.setOnClickListener {
                // Kullanıcı beğeniyi kaldırmak istiyor
                post.likedBy.remove(currentUserId)
                post.likesCount--

                // Görünümde beğenme durumu güncelleniyor
                binding.likeCount.text = post.likesCount.toString()
                binding.likeImage.visibility = View.VISIBLE
                binding.unlikeImage.visibility = View.GONE

                // Veritabanına işlemi kaydedelim (Firestore'dan da kaldıralım)
                userViewModel.likeOrUnLikePost(post.id, currentUserId, false)

                // Adapter'ı güncelle
                val position = getPositionForPost(post.id)
                if (position != -1) {
                    notifyItemChanged(position)
                }
            }

            binding.postUsername.setOnClickListener{
                onUsernameClick(post.username)
            }
            // Load the post image using Glide

        }
        init {
            binding.root.setOnTouchListener { v, event ->
                gestureDetector.onTouchEvent(event)
                v.performClick() // Tıklama olayı için performClick çağır
                true // Olayın doğru şekilde işlenmesi için true döndür
            }
        }
        private val gestureDetector = GestureDetector(binding.root.context, object : GestureDetector.SimpleOnGestureListener() {
            private var lastTapTime = 0L // Son tıklama zamanını saklamak için bir değişken

            override fun onDoubleTap(e: MotionEvent): Boolean {
                val currentTime = System.currentTimeMillis()

                // Eğer son tıklamadan 500 ms geçmemişse, işlemi iptal et
                if (currentTime - lastTapTime < 500) {
                    Log.d("PostAdapter", "Çok hızlı tıklama tespit edildi. İşlem iptal.")
                    return false
                }

                lastTapTime = currentTime // Yeni tıklama zamanını güncelle

                // Kalp animasyonunu başlat
                showHeartAnimation()

                // Animasyon bitene kadar bekle
                Handler(Looper.getMainLooper()).postDelayed({
                    // Geçerli adapterPosition'ı kontrol et
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val post = posts[position]

                        // Eğer post daha önce beğenilmemişse beğenme işlemini başlat
                        if (!post.likedBy.contains(currentUserId)) {
                            // Kullanıcı beğenme işlemi yapmamışsa:
                            post.likedBy.add(currentUserId) // Kullanıcıyı likedBy listesine ekle
                            post.likesCount++ // Beğeni sayısını artır

                            // Görünümde (UI) beğeniyi güncelle
                            binding.likeCount.text = post.likesCount.toString()
                            binding.likeImage.visibility = View.GONE
                            binding.unlikeImage.visibility = View.VISIBLE

                            // Beğenme işlemi veritabanına kaydedilsin (FireStore veya benzeri)
                            userViewModel.likeOrUnLikePost(post.id, currentUserId, true)

                            // Adapter'ı güncelleyerek doğru postu yeniden çiz
                            notifyItemChanged(position)
                        }
                    }
                }, 500) // Animasyon süresi (500ms)

                return true
            }
        })


        fun getPositionForPost(postId: String): Int {
            return posts.indexOfFirst { it.id == postId }  // Post id'sine göre pozisyonu bulur
        }


        private fun showHeartAnimation() {
            val heart = binding.heartImageView
            heart.visibility = View.VISIBLE

            heart.scaleX = 1.0f // Başlangıçta normal boyutta
            heart.scaleY = 1.0f
            heart.alpha = 1.0f // Başlangıçta tamamen opak

            heart.animate()
                .scaleX(3.0f) // Kalp büyüsün
                .scaleY(3.0f)
                .alpha(0.0f) // Saydamlık tamamen
                .setDuration(600) // Büyüme ve saydamlaşma süresi
                .withEndAction {
                    heart.visibility = View.GONE // Animasyonun sonunda gizle
                }
                .start()
        }



    }

    fun setPosts(newPosts: List<Post>) {
        posts = newPosts//+mockItem
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
            val userList =
                result?.getOrNull() // Keskulla profil resmini al
            holder.bind(post, userList) // Post ve profil resmini bağla
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
