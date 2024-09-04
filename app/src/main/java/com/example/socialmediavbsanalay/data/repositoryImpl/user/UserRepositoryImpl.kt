package com.example.socialmediavbsanalay.data.repositoryImpl.user

import com.example.socialmediavbsanalay.data.dataSource.user.UserDataSource
import com.example.socialmediavbsanalay.data.repository.user.UserRepository
import com.example.socialmediavbsanalay.domain.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : UserRepository {

    override fun getUsers(callback: (List<User>) -> Unit) {
        val usersRef = firebaseDatabase.getReference("users")

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { users.add(it) }
                }
                callback(users)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        })
    }

    override fun searchUsersByName(name: String): Query {
        return firebaseDatabase.getReference("users")
            .orderByChild("email")
            .startAt(name)
            .endAt(name + "\uf8ff")
    }
}
