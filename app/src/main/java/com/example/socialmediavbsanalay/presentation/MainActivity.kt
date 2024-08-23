package com.example.socialmediavbsanalay.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialmediavbsanalay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            // Add the WelcomeFragment to the container
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, WelcomeFragment())
                .commit()
        }

    }
}