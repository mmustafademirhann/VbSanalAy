package com.example.socialmediavbsanalay.presentation.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.databinding.ItemGalleryImageBinding
import com.example.socialmediavbsanalay.domain.model.Gallery
import javax.inject.Inject

class GalleryAdapter @Inject constructor(
) : ListAdapter<Gallery, GalleryAdapter.GalleryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class GalleryViewHolder(private val binding: ItemGalleryImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Gallery) {
            // Use someDependency if necessary
            Glide.with(binding.galleryImageView.context)
                .load(item.uri)
                .into(binding.galleryImageView)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Gallery>() {
        override fun areItemsTheSame(oldItem: Gallery, newItem: Gallery): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Gallery, newItem: Gallery): Boolean {
            return oldItem == newItem
        }
    }
}
