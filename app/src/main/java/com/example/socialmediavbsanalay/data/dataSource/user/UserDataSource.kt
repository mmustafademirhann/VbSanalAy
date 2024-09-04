package com.example.socialmediavbsanalay.data.dataSource.user

import com.google.firebase.database.Query

interface UserDataSource {
    fun searchUsersByName(name: String): Query
}