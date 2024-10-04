package com.example.socialmediavbsanalay.presentation.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.databinding.StoryTemplateBinding
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoryAdapter(
    private var stories: List<Story>,
    private val currentUser: String, // Şu anki kullanıcının adı
    private val onStoryClick: (Int) -> Unit,
    private val onUploadStoryClick: () -> Unit, // Hikaye yükleme butonu için
    private val galleryViewModel: GalleryViewModel,
    private var requestPermissionLauncher: ActivityResultLauncher<String>,
    private var imagePickerLauncher: ActivityResultLauncher<Intent>
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

                    // Eğer giriş yapan kullanıcı kendi hikayesi ise 'storyadder' görünür hale getirilir
                    if (it.id == currentUser) {
                        binding.storyAdder.visibility = View.VISIBLE
                    } else {
                        binding.storyAdder.visibility = View.GONE
                    }
                } ?: run {
                    // Kullanıcı bulunamazsa veya hata varsa bir işlem yap
                    binding.storyUsername.text = "Kullanıcı bulunamadı"
                    binding.storyAdder.visibility = View.GONE // Hata durumunda storyAdder gizlenir
                }
            }

            binding.root.setOnClickListener {
                onUploadStoryClick() // Hikaye yükleme işlemi
            }
            binding.storyAdder.setOnClickListener{
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }


    inner class OtherUserViewHolder(private val binding: StoryTemplateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.storyUsername.text = story.ownerUser // Hikayeyi paylaşan kullanıcı adı
            Glide.with(binding.storyImageView.context)
                .load(story.imageUrl)
                .circleCrop()
                .into(binding.storyImageView)

            binding.storyAdder.visibility = View.GONE // Diğer kullanıcılar için 'storyAdder' gizli

            binding.root.setOnClickListener {
                // Hikaye tıklandığında yapılacaklar
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
        if (position == 0) { // İlk pozisyon giriş yapan kullanıcının hikayesidir
            (holder as CurrentUserViewHolder).bind()
        } else { // Diğer kullanıcıların hikayeleri
            val story = stories[position - 1] // stories, diğer kullanıcı hikayelerini içeren liste
            (holder as OtherUserViewHolder).bind(story)
        }
    }

    override fun getItemCount(): Int = stories.size + 1 // Kendi hikayenizi ekleyin

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_CURRENT_USER else VIEW_TYPE_OTHER_USER
    }
}
