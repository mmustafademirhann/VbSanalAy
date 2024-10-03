package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.StoryTemplateBinding
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoryAdapter(
    private var stories: List<Story>,
    private val currentUser: String, // Şu anki kullanıcının adı
    private val onStoryClick: (Int) -> Unit,
    private val onUploadStoryClick: () -> Unit, // Hikaye yükleme butonu için
    private val galleryViewModel: GalleryViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_CURRENT_USER = 0
        const val VIEW_TYPE_OTHER_USER = 1
    }
    fun setStories(newStories: List<Story>) {
        this.stories = newStories
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    inner class CurrentUserViewHolder(private val binding: StoryTemplateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            CoroutineScope(Dispatchers.Main).launch {
                val user = galleryViewModel.getUserByIdUsr(currentUser) // Suspend fonksiyonu çağırıyoruz
                user?.let {
                    binding.storyUsername.text = it.id // Kullanıcı adı ekrana yansıtılır
                    Glide.with(binding.storyImageView.context)
                        .load(it.profileImageUrl)
                        .circleCrop()
                        .into(binding.storyImageView)
                } ?: run {
                    // Kullanıcı bulunamazsa veya hata varsa bir işlem yap
                    binding.storyUsername.text = "Kullanıcı bulunamadı"
                }
            }

            binding.root.setOnClickListener {
                onUploadStoryClick() // Hikaye yükleme işlemi
            }
        }
    }

    inner class OtherUserViewHolder(private val binding: StoryTemplateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.storyUsername.text = story.ownerUser
            Glide.with(binding.storyImageView.context)
                .load(story.profileImageUrl)
                .circleCrop()
                .into(binding.storyImageView)

            binding.root.setOnClickListener {
                onStoryClick(adapterPosition) // Hikayeye tıklandığında pozisyonu gönder
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
        if (holder is CurrentUserViewHolder) {
            holder.bind() // Kendi hikayesi için bağla
        } else if (holder is OtherUserViewHolder) {
            holder.bind(stories[position - 1]) // Diğer kullanıcı hikayeleri
        }
    }

    override fun getItemCount(): Int = stories.size + 1 // Kendi hikayenizi ekleyin

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_CURRENT_USER else VIEW_TYPE_OTHER_USER
    }
}
