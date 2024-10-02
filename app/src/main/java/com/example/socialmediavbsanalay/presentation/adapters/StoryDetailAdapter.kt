package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediavbsanalay.databinding.StoryDetailTemplateBinding
import com.example.socialmediavbsanalay.domain.model.Story


class StoryDetailAdapter : ListAdapter<Story, StoryDetailAdapter.StoryViewHolder>(StoryDiffCallback()) {

    inner class StoryViewHolder(private val binding: StoryDetailTemplateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            // Hikaye ile ilgili verileri bağlayın (örneğin, görüntü ve kullanıcı adı)

            // Diğer hikaye verilerini buraya ekleyin
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryDetailTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StoryDiffCallback : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id // ID'leri kontrol et
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem // İçeriklerin aynı olup olmadığını kontrol et
        }
    }
}
