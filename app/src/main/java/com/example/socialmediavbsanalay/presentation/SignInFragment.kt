package com.example.socialmediavbsanalay.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentSignInBinding
import com.example.socialmediavbsanalay.databinding.FragmentWelcomeBinding
import com.example.socialmediavbsanalay.presentation.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private val hideHandler = Handler(Looper.myLooper()!!)
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()



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

        binding.signInButton.setOnClickListener{
            val email=binding.editTextMail.text.toString()
            val password=binding.editTextNumberPassword.text.toString()
            authViewModel.signIn(email,password)
        }
        authViewModel.authState.observe(viewLifecycleOwner){result->
            result.onSuccess { user ->
                navigateToMainPage(view)

            }.onFailure { exception ->
                if (exception.message == "User not found") {
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Sign-in failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.createTextView.setOnClickListener{
            navigateToSignUp(it)
        }

    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    fun navigateToSignUp(view:View){
        val action =SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun navigateToMainPage(view:View){
       val action =SignInFragmentDirections.actionSignInFragmentToMainPageFragment()
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