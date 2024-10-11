package com.example.socialmediavbsanalay.presentation.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.databinding.ActivityStoryBinding
import com.example.socialmediavbsanalay.domain.model.UserStories
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import javax.inject.Inject

@AndroidEntryPoint
class StoryActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private lateinit var binding: ActivityStoryBinding
    private val userViewModel: UserViewModel by viewModels()
    private var userStories: ArrayList<UserStories>? = null
    private var adapterPosition = 0
    private var storyPosition = 0
    private lateinit var storyProgressView: StoriesProgressView

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the data from the intent
        intent?.let {
            userStories = it.getParcelableArrayListExtra(USER_STORIES)
            adapterPosition = it.getIntExtra(ADAPTER_POSITION, 0)
            storyPosition = it.getIntExtra(STORY_POSITION, 0)
        }

        setClickListeners()
        loadProfileAndName(userStories?.get(adapterPosition))

        // Setup stories
        setupStories(userStories?.get(adapterPosition))

        loadImageWithGlide(storyPosition, adapterPosition)
    }

    private fun loadImageWithGlide(position: Int, userIndex: Int) {
        userViewModel.updateSeenStatusOfStory(userStories?.get(userIndex)?.stories?.get(position), userPreferences.getUser()?.id ?: "")
        Glide.with(this)
            .load(userStories?.get(userIndex)?.stories?.get(position)?.imageUrl)
            .into(binding.storyImage)
    }

    private fun setupStories(userStories: UserStories?) {
        storyProgressView = StoriesProgressView(this)
        binding.spwContainer.removeAllViews()
        binding.spwContainer.addView(storyProgressView)
        storyProgressView.setStoriesCount(userStories?.stories?.size ?: 0)
        storyProgressView.setStoryDuration(5000L)
        storyProgressView.setStoriesListener(this)
        storyProgressView.startStories(storyPosition)
    }

    private fun setClickListeners() {
        binding.leftView.setOnClickListener {
            if (storyPosition == 0 && adapterPosition == 0) {
                //no-op
            } else {
                storyProgressView.reverse()
            }
        }

        binding.rightView.setOnClickListener {
            storyProgressView.skip()
        }

        binding.ivClose.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun loadProfileAndName(userStories: UserStories?) {
        Glide.with(this)
            .load(userStories?.ownerUserProfileImage)
            .circleCrop()
            .into(binding.ivOwner)
        binding.tvOwner.text = userStories?.ownerUser
    }

    override fun onNext() {
        loadImageWithGlide(++storyPosition, adapterPosition)
    }

    override fun onPrev() {
        if (storyPosition != 0) {
            loadImageWithGlide(--storyPosition, adapterPosition)
        } else {
            --adapterPosition
            storyPosition = (userStories?.get(adapterPosition))?.stories?.size?.minus(1) ?: 0
            loadProfileAndName(userStories?.get(adapterPosition))
            setupStories(userStories?.get(adapterPosition))
            loadImageWithGlide(userStories?.get(adapterPosition)?.stories?.size?.minus(1) ?: 0, adapterPosition)
        }
    }

    override fun onComplete() {
        if (adapterPosition + 1 == userStories?.size) {
            this.setResult(RESULT_OK)
            finish()
        } else {
            ++adapterPosition
            storyPosition = 0
            loadProfileAndName(userStories?.get(adapterPosition))
            setupStories(userStories?.get(adapterPosition))
            loadImageWithGlide(0, adapterPosition)
        }
    }

    override fun onDestroy() {
        storyProgressView.destroy()
        super.onDestroy()
    }

    companion object {
        private const val USER_STORIES = "bundle_user_stories"
        private const val ADAPTER_POSITION = "bundle_adapter_position"
        private const val STORY_POSITION = "bundle_story_position"

        // Function to start the StoryActivity
        fun start(context: Context, userStories: ArrayList<UserStories>?, adapterPosition: Int, storyPosition: Int): Intent {
            return Intent(context, StoryActivity::class.java).apply {
                putExtra(USER_STORIES, userStories)
                putExtra(ADAPTER_POSITION, adapterPosition)
                putExtra(STORY_POSITION, storyPosition)
            }
        }
    }
}