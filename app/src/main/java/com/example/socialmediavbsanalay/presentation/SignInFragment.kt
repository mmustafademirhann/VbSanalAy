package com.example.socialmediavbsanalay.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentSignInBinding
import com.example.socialmediavbsanalay.databinding.FragmentWelcomeBinding


class SignInFragment : Fragment() {
    private val hideHandler = Handler(Looper.myLooper()!!)
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createTextView.setOnClickListener{
            next(it)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    fun next(view:View){
        val action =SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        Navigation.findNavController(view).navigate(action)
    }
    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.decorView?.systemUiVisibility = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}