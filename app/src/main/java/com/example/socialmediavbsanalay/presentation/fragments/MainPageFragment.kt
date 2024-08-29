package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentMainPageBinding
import com.example.socialmediavbsanalay.presentation.adapters.PostAdapter
import com.example.socialmediavbsanalay.presentation.adapters.StoryAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [MainPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainPageFragment : Fragment() {

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storyAdapter = StoryAdapter()

        binding.storyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = storyAdapter
        }
        postAdapter=PostAdapter()

        binding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = postAdapter
        }

        // Load your stories into the adapter




        val stories =storyAdapter.loadStories() // Implement this function to get your list of stories
        storyAdapter.setStories(stories)

        val posts =postAdapter.loadPosts() // Implement this function to get your list of stories
        postAdapter.setPosts(posts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}