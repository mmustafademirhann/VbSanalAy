package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.socialmediavbsanalay.databinding.FragmentSignUpBinding
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.viewModels.AuthViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database("https://sanalay-b69cd-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("message")

    val user = hashMapOf(
        "first" to "Ada",
        "last" to "Lovelace",
        "born" to 1815,
    )

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
        (activity as MainActivity).hideBottomBar()
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
            authViewModel.createUser()

            myRef.setValue("Hello, World!").addOnSuccessListener {
                Toast.makeText(requireContext(), "Sucess", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e->
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}