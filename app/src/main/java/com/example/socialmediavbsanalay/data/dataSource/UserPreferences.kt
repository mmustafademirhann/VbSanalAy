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

    // Save user object to SharedPreferences
    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString("user", userJson).apply()
    }

    // Retrieve user object from SharedPreferences
    fun getUser(): User? {
        val userJson = sharedPreferences.getString("user", null)
        return userJson?.let { gson.fromJson(it, User::class.java) }
    }

    //TODO user informations will be deleted after logout
    fun deleteUser() {
        sharedPreferences.edit().clear().apply()
    }
}
