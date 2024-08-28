package com.example.socialmediavbsanalay.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        var xyz=true
        setContentView(view)
        if (savedInstanceState == null) {
            // Show ProgressBar


            // Simulate loading (e.g., navigation setup)

        }
        //what ı need to do ?
        //firebase--->I connected
        //after that writing codes for firebase ----> ı did not right now
        //login things code for firebase --> ı did not

    }
}