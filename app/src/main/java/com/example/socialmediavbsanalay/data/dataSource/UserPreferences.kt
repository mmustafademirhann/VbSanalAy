package com.example.socialmediavbsanalay.data.dataSource

import android.content.Context
import android.content.SharedPreferences
import com.example.socialmediavbsanalay.domain.model.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    // Save user object to SharedPreferences (Login işlemi)
    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString("user", userJson).apply()
    }

    // Retrieve user object from SharedPreferences
    fun getUser(): User? {
        val userJson = sharedPreferences.getString("user", null)
        return userJson?.let { gson.fromJson(it, User::class.java) }
    }

    // Delete user information from SharedPreferences (Logout işlemi)
    fun deleteUser() {
        sharedPreferences.edit().clear().apply()
    }

    // Login: Save the user information after successful authentication
    fun login(user: User) {
        saveUser(user)  // User bilgilerini kaydeder
    }

    // Check if user is logged in (optional, useful for checking user's status)
    fun isLoggedIn(): Boolean {
        return getUser() != null
    }
}
