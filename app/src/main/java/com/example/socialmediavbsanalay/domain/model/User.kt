package com.example.socialmediavbsanalay.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val surName: String = "",
    val email: String = "",
    val gender:String="",
    val profileImageUrl:String="",
    val profileBacgroundImageUrl:String="",
    val stories: List<Story> = listOf()
)