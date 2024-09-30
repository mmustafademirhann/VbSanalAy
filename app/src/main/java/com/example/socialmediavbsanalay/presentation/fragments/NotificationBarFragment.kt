package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.socialmediavbsanalay.databinding.FragmentNotificationBarBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationBarFragment : Fragment() {

    private var _binding: FragmentNotificationBarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBarBinding.inflate(inflater, container, false)
        return binding.root
    }





}