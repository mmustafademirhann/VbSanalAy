package com.example.socialmediavbsanalay.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val surName: String = "",
    val email: String = "",
    val gender:String="",
    val profileImageUrl:String="",
    val profileBacgroundImageUrl:String="",
    val stories: List<Story> = listOf(),
    val followers: List<String> = listOf(),  // Onu takip edenlerin ID'leri
    val following: List<String> = listOf(),   // Takip ettiği kişilerin ID'leri
    val followerCount: Int = 0,
    val followingCount: Int = 0
)


