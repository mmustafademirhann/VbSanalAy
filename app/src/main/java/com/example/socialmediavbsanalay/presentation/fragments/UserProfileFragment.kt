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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    private lateinit var userPostAdapter: UserPostAdapter
    private lateinit var binding: FragmentUserProfileBinding
    private val galleryViewModel: GalleryViewModel by viewModels()
    val itemDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val spacing = 0 // spacing değerini dp cinsinden ayarlayın
            outRect.set(spacing, spacing, spacing, spacing)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        // Initialize RecyclerView and Adapter
        userAdapter = UserAdapter()

        // Set up RecyclerView


        // Observe users StateFlow
        lifecycleScope.launch {
            userViewModel.users.collect { userList ->
                userAdapter.updateUsers(userList)
            }
        }

        // Load all users initially
        userViewModel.fetchAllUsers()

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPostAdapter = UserPostAdapter()
        galleryViewModel.posts.observe(viewLifecycleOwner) { posts_for_users ->
            val filteredPosts = posts_for_users.filter { post -> post.username ==galleryViewModel.x}
            userPostAdapter.setPosts(filteredPosts)
        }

        binding.recyclerViewPosts.apply {
            layoutManager = GridLayoutManager(context, 3) // 3 sütunlu grid
            adapter = userPostAdapter




            //addItemDecoration(GridSpacingItemDecoration(3, 10, true)) // 10dp boşluk, kenarları dahil
        }

    }
}
