package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.databinding.FragmentPostDetailBinding
import com.example.socialmediavbsanalay.presentation.adapters.PostAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailBinding
    private val galleryViewModel: GalleryViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter
    private val userViewModel: UserViewModel by viewModels()

    companion object {
        private const val ARG_USER_ID = "userId"

        fun newInstance(userId: String): PostDetailFragment {
            return PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter'ı başlat
        /////////////////postAdapter = PostAdapter(userViewModel, onCommentClick = )

        // RecyclerView ayarlarını yap
        binding.postsRecyclerViewForDetails.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }

        // ViewModel'deki postları gözlemle
        galleryViewModel.posts.observe(viewLifecycleOwner) { posts ->
            val userId = arguments?.getString(ARG_USER_ID) ?: ""
            // userId'sine göre gönderileri filtrele
            val filteredPosts = posts.filter { post ->
                post.username == userId
            }

            // Filtrelenen postları adapter'a ver
            postAdapter.setPosts(filteredPosts)
        }

        // Swipe-to-refresh işlemini başlat
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshGallery()
        }
    }

    private fun refreshGallery() {
        viewLifecycleOwner.lifecycleScope.launch {
            galleryViewModel.refreshGallery()
            binding.swipeRefreshLayout.isRefreshing = false // Refresh tamamlandığında animasyonu durdur
        }
    }
}
