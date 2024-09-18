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
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    private lateinit var userPostAdapter: UserPostAdapter
    private lateinit var binding: FragmentUserProfileBinding
    private val galleryViewModel: GalleryViewModel by viewModels()

    private var isOwner: Boolean = false

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
        private const val ARG_USER_ID = "user_id"
        private const val ARG_IS_FROM_SEARCH = "is_from_search"

        fun newInstance(userId: String, isFromSearch: Boolean): UserProfileFragment {
            val fragment = UserProfileFragment()
            val args = Bundle()
            args.putString(ARG_USER_ID, userId)
            args.putBoolean(ARG_IS_FROM_SEARCH, isFromSearch)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize user post adapter
        userPostAdapter = UserPostAdapter()

        // Observe posts and filter by username
        galleryViewModel.posts.observe(viewLifecycleOwner) { postsForUsers ->
            val filteredPosts = postsForUsers.filter { post -> post.username == galleryViewModel.IDGET }
            userPostAdapter.setPosts(filteredPosts)
        }

        val isFromSearch = arguments?.getBoolean(ARG_IS_FROM_SEARCH) ?: false
        isOwner = !isFromSearch // searchFragment'den gelmediyse true, geldiyse false

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
        if (isOwner) {
            binding.settingsIcon.visibility = View.VISIBLE
            binding.followButton.visibility = View.GONE
            binding.messageButton.visibility = View.GONE
        } else {
            binding.settingsIcon.visibility = View.GONE
            binding.followButton.visibility = View.VISIBLE
            binding.messageButton.visibility = View.VISIBLE
        }
    }
}
