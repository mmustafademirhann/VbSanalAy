package com.example.socialmediavbsanalay.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentSignInBinding
import com.example.socialmediavbsanalay.databinding.FragmentSignUpBinding
import com.example.socialmediavbsanalay.presentation.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view=binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.signUpResult.observe(viewLifecycleOwner){result->
            result.fold(
                onSuccess = { user ->
                    if (user != null) {
                        Toast.makeText(context, "User created successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "User creation failed", Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = { exception ->
                    Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
        binding.btnRegister.setOnClickListener {
            authViewModel.signUp(
                binding.etMail.text.toString(),
                binding.editTextNumberPassword2.text.toString()
            )
        }
    }

}