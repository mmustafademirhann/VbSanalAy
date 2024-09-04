package com.example.yourapp.data.dataSourceImpl.user

import com.example.socialmediavbsanalay.data.dataSource.user.UserDataSource
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val database: FirebaseDatabase
) : UserDataSource {

    override fun searchUsersByName(name: String): Query {
        return database.getReference("users")
            .orderByChild("name")
            .startAt(name)
            .endAt(name + "\uf8ff")
    }
}
