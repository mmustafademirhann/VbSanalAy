package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentMainPageBinding
import com.example.socialmediavbsanalay.databinding.FragmentMessageBinding
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeImageViewM.setOnClickListener {
            navigateToMain(it)
        }
        binding.notificationImageViewM.setOnClickListener {
            navigateToNotification(it)
        }
        binding.userImageViewM.setOnClickListener {
            navigateToUserProfile(it)
        }
    }
    fun navigateToMain(view: View){
        val action =MessageFragmentDirections.actionMessageFragmentToMainPageFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun navigateToUserProfile(view: View){
        val action =MessageFragmentDirections.actionMessageFragmentToUserProfileFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun navigateToNotification(view: View){
        val action =MessageFragmentDirections.actionMessageFragmentToNotificationBarFragment()
        Navigation.findNavController(view).navigate(action)
    }

}