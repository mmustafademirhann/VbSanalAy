package com.example.socialmediavbsanalay.data.repository.user

import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.database.Query

interface UserRepository {
    fun getUsers(callback: (List<User>) -> Unit)
    fun searchUsersByName(name: String): Query
}
