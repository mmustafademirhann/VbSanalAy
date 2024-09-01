package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentMessageBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeImageViewN.setOnClickListener {
            navigateToMain(it)
        }
        binding.messageImageViewN.setOnClickListener {
            navigateToMessage(it)
        }
        binding.userImageViewN.setOnClickListener {
            navigateToUserProfile(it)
        }
    }
    fun navigateToMain(view: View){
        val action =NotificationBarFragmentDirections.actionNotificationBarFragmentToMainPageFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun navigateToUserProfile(view: View){
        val action =NotificationBarFragmentDirections.actionNotificationBarFragmentToUserProfileFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun navigateToMessage(view: View){
        val action =NotificationBarFragmentDirections.actionNotificationBarFragmentToMessageFragment()
        Navigation.findNavController(view).navigate(action)
    }

}