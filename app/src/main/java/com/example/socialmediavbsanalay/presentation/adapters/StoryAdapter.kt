package com.example.socialmediavbsanalay.presentation.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.StoryTemplateBinding
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.domain.model.UserStories
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoryAdapter(
    private var userStories: ArrayList<UserStories>,
    private val currentUser: String, // Şu anki kullanıcının adı
    private val userViewModel: UserViewModel,
    private val onStoryClick: (isUploadOperation: Boolean, userStories: UserStories?, adapterPosition: Int, storyPosition: Int?) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_CURRENT_USER = 0
        const val VIEW_TYPE_OTHER_USER = 1
    }
    fun setStories(userStories: ArrayList<UserStories>) {
        this.userStories = userStories
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    fun getStories():ArrayList<UserStories> {
        return userStories
    }


    inner class CurrentUserViewHolder(private val binding: StoryTemplateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userStories: UserStories,users: List<User>?) {
            val matchingUser = users?.find { user -> user.id == userStories.ownerUser }
            var isSeenAll = true
            CoroutineScope(Dispatchers.Main).launch {
                binding.storyUsername.text =
                    userStories.ownerUser // Kullanıcı adı ekrana yansıtılır
                if (matchingUser != null) {
                    Glide.with(binding.storyImageView.context)
                        .load(matchingUser.profileImageUrl)
                        .circleCrop()
                        .into(binding.storyImageView)
                }
                binding.storyAdder.visibility = View.VISIBLE
                if (userStories.stories.isEmpty()) {
                    binding.flStory.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.elipseforstory)
                } else {
                    userStories.stories.forEach {
                        if (it.seenUsers?.contains(currentUser) == true) {
                            //no-op
                        } else {
                            isSeenAll = false
                        }
                    }

                    if (isSeenAll) {
                        binding.flStory.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.seen_circle)
                    } else {
                        binding.flStory.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.unseen_circle)
                    }
                }
            }
            binding.storyAdder.setOnClickListener {
                onStoryClick(true, null, adapterPosition, null)
            }
            binding.storyImageView.setOnClickListener {
                if (userStories.stories.isEmpty()) {
                    //no-op
                } else {
                    val storyPosition: Int = if (isSeenAll) {
                        0
                    } else {
                        userStories.stories.indexOfFirst {
                            it.seenUsers?.contains(currentUser) != true
                        }
                    }
                    onStoryClick(false, userStories, adapterPosition, storyPosition)
                }
            }
        }
    }

    inner class OtherUserViewHolder(private val binding: StoryTemplateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userStories: UserStories,users: List<User>?) {
            val matchingUser = users?.find { user -> user.id == userStories.ownerUser }
            binding.storyUsername.text = userStories.ownerUser // Hikayeyi paylaşan kullanıcı adı
            if (matchingUser != null) {
                Glide.with(binding.storyImageView.context)
                    .load(matchingUser.profileImageUrl)
                    .circleCrop()
                    .into(binding.storyImageView)
            }

            var isSeenAll = true
            userStories.stories.forEach {
                if (it.seenUsers?.contains(currentUser) == true) {
                    //no-op
                } else {
                    isSeenAll = false
                }
            }

            if (isSeenAll) {
                binding.flStory.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.seen_circle)
            } else {
                binding.flStory.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.unseen_circle)
            }

            binding.storyAdder.visibility = View.GONE // Diğer kullanıcılar için 'storyAdder' gizli

            binding.root.setOnClickListener {
                val storyPosition: Int = if (isSeenAll) {
                    0
                } else {
                    userStories.stories.indexOfFirst {
                        it.seenUsers?.contains(currentUser) != true
                    }
                }
                if (this@StoryAdapter.userStories[0].stories.isEmpty()) {
                    this@StoryAdapter.userStories.removeAt(0)
                    onStoryClick(false, userStories, adapterPosition - 1, storyPosition)
                } else {
                    onStoryClick(false, userStories, adapterPosition, storyPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CURRENT_USER -> {
                val binding = StoryTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CurrentUserViewHolder(binding)
            }
            VIEW_TYPE_OTHER_USER -> {
                val binding = StoryTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OtherUserViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userStory = userStories[position] // stories, diğer kullanıcı hikayelerini içeren liste
        userViewModel.getAllUsers()
        if (holder.itemViewType == VIEW_TYPE_CURRENT_USER) { // İlk pozisyon giriş yapan kullanıcının hikayesidir
            // Öncelikle kullanıcıyı çek

            userViewModel.usersListt.observeForever { result -> // LiveData'yı gözlemle
                val userList =
                    result?.getOrNull() // Keskulla profil resmini al
                (holder as CurrentUserViewHolder).bind(userStory,userList)
               // holder.bind(post, userList) // Post ve profil resmini bağla
            }


        } else { // Diğer kullanıcıların hikayeleri
            userViewModel.usersListt.observeForever { result -> // LiveData'yı gözlemle
                val userList =
                    result?.getOrNull() // Keskulla profil resmini al
                (holder as OtherUserViewHolder).bind(userStory,userList)
                //(holder as CurrentUserViewHolder).bind(userStory,userList)
                // holder.bind(post, userList) // Post ve profil resmini bağla
            }

        }
    }

    override fun getItemCount(): Int = userStories.size

    override fun getItemViewType(position: Int): Int {
        return if (userStories[position].ownerUser == currentUser) VIEW_TYPE_CURRENT_USER else VIEW_TYPE_OTHER_USER
    }
}
