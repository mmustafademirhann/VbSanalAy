package com.example.socialmediavbsanalay.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediavbsanalay.databinding.ItemOptionBinding
import javax.inject.Inject

class OptionsAdapter @Inject constructor(): RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    var onItemClickListener: ((String) -> Unit)? = null
    private val options = listOf("Camera", "Gallery")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.binding.optionTextView.text = option
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(option)
        }
    }

    override fun getItemCount(): Int = options.size

    inner class ViewHolder(val binding: ItemOptionBinding) : RecyclerView.ViewHolder(binding.root)
}