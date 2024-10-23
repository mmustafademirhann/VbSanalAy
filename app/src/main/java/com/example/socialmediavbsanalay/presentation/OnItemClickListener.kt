package com.example.socialmediavbsanalay.presentation

import com.example.socialmediavbsanalay.domain.model.Post

interface OnItemClickListener {
    fun onItemClicked(post: Post, position: Int)
}
