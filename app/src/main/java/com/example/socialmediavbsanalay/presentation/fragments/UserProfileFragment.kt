package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentMessageBinding
import com.example.socialmediavbsanalay.databinding.FragmentUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeIconU.setOnClickListener {
            navigateToMain(it)
        }
        binding.notificationU.setOnClickListener {
            navigateToNotification(it)
        }
        binding.messageIconU.setOnClickListener {
            navigateToMessage(it)
        }
    }
    fun navigateToMain(view: View){
        val action =UserProfileFragmentDirections.actionUserProfileFragmentToMainPageFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun navigateToMessage(view: View){
        val action =UserProfileFragmentDirections.actionUserProfileFragmentToMessageFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun navigateToNotification(view: View){
        val action =UserProfileFragmentDirections.actionUserProfileFragmentToNotificationBarFragment()
        Navigation.findNavController(view).navigate(action)
    }

}