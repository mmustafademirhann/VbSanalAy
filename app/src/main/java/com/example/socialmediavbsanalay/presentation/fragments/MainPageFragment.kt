package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentMainPageBinding
import com.example.socialmediavbsanalay.presentation.adapters.PostAdapter
import com.example.socialmediavbsanalay.presentation.adapters.StoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [MainPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainPageFragment : Fragment() {

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter
    private val galleryViewModel: GalleryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()


    private fun navigateToSearchResultsFragment() {
        val searchUserPostFragment = SearchUserPostFragment()
        replaceFragment(searchUserPostFragment)
    }


    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment) // Use your actual container ID
            .addToBackStack(null) // Optional, if you want back navigation
            .commit()
    }



    companion object {
        private const val GALLERY_REQUEST_CODE = 123
        //private val REQUEST_IMAGE_CAPTURE = 1
        //private val PICK_IMAGE = 2
        //private val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        postAdapter = PostAdapter(userViewModel)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).showBottomBar()

        galleryViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.setPosts(posts)
        }

        val recyclerView = binding.postsRecyclerView
        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.adapter=postAdapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                galleryViewModel.refreshGallery()
                binding.swipeRefreshLayout.isRefreshing = false // Refresh tamamlandığında animasyonu durdurun
            }
        }

        adapterFunctions()
        // Load your stories into the adapter
        binding.editTextText3.setOnClickListener {
            navigateToSearchResultsFragment()
        }


    }
    private fun navigateToSame(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    private fun adapterFunctions() {
        storyAdapter = StoryAdapter()

        binding.storyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = storyAdapter
        }
        postAdapter = PostAdapter(userViewModel)

        binding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = postAdapter
        }
        val stories =
            storyAdapter.loadStories() // Implement this function to get your list of stories
        storyAdapter.setStories(stories)

    }


    // ContextCompat.checkSelfPermission(
    // requireContext(),


}