package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.StoryTemplateBinding
import com.example.socialmediavbsanalay.domain.model.Story
import javax.inject.Inject

class StoryAdapter @Inject constructor() : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private var stories: List<Story> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    fun setStories(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }
    fun loadStories(): List<Story> {
        return listOf(
            Story(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Story(imageResId = R.drawable.shin, username = "user2"),
            Story(imageResId = R.drawable.shin, username = "user3"),
            Story(imageResId = R.drawable.shin, username = "user4"),
            Story(imageResId = R.drawable.shin, username = "user5"),
            Story(imageResId = R.drawable.aysegulmustafa, username = "user1"),
            Story(imageResId = R.drawable.shin, username = "user2"),
            Story(imageResId = R.drawable.shin, username = "user3"),
            Story(imageResId = R.drawable.shin, username = "user4"),
            Story(imageResId = R.drawable.shin, username = "user5")


        )
    }

    class StoryViewHolder(private val binding: StoryTemplateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.storyImageView.setImageResource(story.imageResId) // Example
            binding.storyUsername.text = story.username
        }

    }
}
