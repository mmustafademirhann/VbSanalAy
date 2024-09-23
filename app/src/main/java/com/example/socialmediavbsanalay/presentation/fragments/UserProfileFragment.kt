package com.example.socialmediavbsanalay.presentation.fragments

import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentUserProfileBinding
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.OnItemClickListener
import com.example.socialmediavbsanalay.presentation.adapters.PostAdapter
import com.example.socialmediavbsanalay.presentation.adapters.UserAdapter
import com.example.socialmediavbsanalay.presentation.adapters.UserPostAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile), OnItemClickListener {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    private lateinit var userPostAdapter: UserPostAdapter
    private lateinit var binding: FragmentUserProfileBinding
    private val galleryViewModel: GalleryViewModel by viewModels()
    private lateinit var userId: String
    private lateinit var ownerUser:String

    //private var isOwner: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        // Initialize UserAdapter with onItemClick lambda
        userAdapter = UserAdapter { userId ->
            // Handle item click, e.g., show a Toast or navigate to user profile
            Toast.makeText(context, "Clicked user: $userId", Toast.LENGTH_SHORT).show()
            // Alternatively, navigate to user profile
            // navigateToUserProfile(userId)
        }

        // Initialize RecyclerView and Adapter
        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        // Observe user list and update the adapter
        lifecycleScope.launch {
            userViewModel.users.collect { userList ->
                userAdapter.updateUsers(userList)
            }
        }

        // Load all users
        userViewModel.fetchAllUsers()

        return binding.root
    }

    companion object {
        private const val ARG_USER_ID = "userId"
        private const val ARG_IS_FROM_SEARCH = "is_from_search"
        fun newInstance(userId: String): PostDetailFragment {
            val fragment = PostDetailFragment()
            val args = Bundle().apply {
                putString(ARG_USER_ID, userId)
            }
            fragment.arguments = args
            return fragment
        }

        fun newInstance(userId: String, isFromSearch: Boolean): UserProfileFragment {
            val fragment = UserProfileFragment()
            val args = Bundle().apply {
                putString(ARG_USER_ID, userId)
                putBoolean(ARG_IS_FROM_SEARCH, isFromSearch)
            }
            fragment.arguments = args
            return fragment
        }
    }
    // Filtreleme işlemi, userId kullanarak postları filtreliyoruz
    private fun observePosts(userId: String) {
        // Postları userId'ye göre filtreleme işlemi
        galleryViewModel.posts.observe(viewLifecycleOwner) { postsForUsers ->
            val filteredPosts = postsForUsers.filter { post -> post.username == userId }
            userPostAdapter.setPosts(filteredPosts)
        }

        // Kullanıcı verilerini gözlemleme işlemi
        galleryViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Kullanıcı bulundu, UI'yi güncelle
                binding.usernameM.text = it.id
                binding.userHandle.text = "@${it.name}" // Kullanıcının handle'ı
                // Kullanıcının profil resmini güncelle
                // Örneğin: Glide ile resim yükleme işlemi yapılabilir
                // Glide.with(this).load(it.profileImageUrl).into(binding.profileImage)
            } ?: run {
                // Eğer kullanıcı bulunamazsa hata mesajı göster
                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
            }
        }

        // Kullanıcıyı userId ile yükleme işlemi
        galleryViewModel.getUserById(userId)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsIcon
            .setOnClickListener{

            }

        val isOwner = arguments?.getBoolean(ARG_IS_FROM_SEARCH, false) ?: false
        if (isOwner){
            userId = arguments?.getString("userId") ?: ""
            if (userId.isNotEmpty()) {
                observePosts(userId)
            }
        }
        else{
            userId = arguments?.getString("userId") ?: ""





            galleryViewModel.posts.observe(viewLifecycleOwner) { postsForUsers ->
                ownerUser=galleryViewModel.IDGET
                val filteredPosts = postsForUsers.filter { post -> post.username == ownerUser }
                userPostAdapter.setPosts(filteredPosts)
                galleryViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                    user?.let {
                        // Kullanıcı bulundu, UI'yi güncelle
                        binding.usernameM.text = it.id
                        binding.userHandle.text = "@${it.name}" // Kullanıcının handle'ı
                        // Kullanıcının profil resmini güncelle
                        // Örneğin: Glide ile resim yükleme işlemi yapılabilir
                        // Glide.with(this).load(it.profileImageUrl).into(binding.profileImage)
                    } ?: run {
                        binding.usernameM.text = galleryViewModel.IDGET
                        binding.userHandle.text = galleryViewModel.IDGET
                    }

                }
                galleryViewModel.getUserById(ownerUser)
            }




        }


        // Initialize user post adapter
        userPostAdapter = UserPostAdapter(this)

        // Observe posts and filter by username

        //val isFromSearch = arguments?.getBoolean(ARG_IS_FROM_SEARCH) ?: false
        //isOwner = !isFromSearch // searchFragment'den gelmediyse true, geldiyse false

        // Observe posts and update UI
        lifecycleScope.launch {
            galleryViewModel.currentUserId.collect { currentUserId ->
                updateUIBasedOnOwnership()
            }
        }

        binding.recyclerViewPosts.apply {
            layoutManager = GridLayoutManager(context, 3) // 3-column grid
            adapter = userPostAdapter
        }
    }

    private fun updateUIBasedOnOwnership() {
        val isOwner=arguments?.getBoolean(ARG_IS_FROM_SEARCH, false) ?: false
        if (!isOwner) {
            binding.settingsIcon.visibility = View.VISIBLE
            binding.followButton.visibility = View.GONE
            binding.messageButton.visibility = View.GONE
        } else {
            binding.settingsIcon.visibility = View.GONE
            binding.followButton.visibility = View.VISIBLE
            binding.messageButton.visibility = View.VISIBLE
        }
    }

    override fun onItemClicked(post:Post) {
        val postDetailFragment = PostDetailFragment.newInstance(userId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, postDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}
