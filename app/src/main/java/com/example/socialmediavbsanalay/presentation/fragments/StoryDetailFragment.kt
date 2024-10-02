package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentStoryDetailBinding
import com.example.socialmediavbsanalay.presentation.adapters.StoryDetailAdapter

class StoryDetailFragment : Fragment() {

    private lateinit var storyDetailAdapter: StoryDetailAdapter
    private lateinit var recyclerView: RecyclerView
    private var ownerUser: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        recyclerView = binding.fotoRecyclerView
        ownerUser = arguments?.getString("ownerUser")

        setupRecyclerView()
        fetchUserStories()

        return binding.root
    }

    private fun setupRecyclerView() {
        storyDetailAdapter = StoryDetailAdapter()
        recyclerView.adapter = storyDetailAdapter
    }

    private fun fetchUserStories() {
        // Belirli kullanıcının hikayelerini çekmek için repository kullanın
        // Örnek: storyViewModel.fetchUserStories(ownerUser).observe(viewLifecycleOwner) { stories ->
        //     storyDetailAdapter.submitList(stories)
        // }
    }
}
