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
        "first" to "d",
        "last" to "d",
        "born" to 2,
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
            val email = binding.etMail.text.toString()
            val password = binding.editTextNumberPassword2.text.toString()
            val name = binding.etName.text.toString()
            val surName = binding.etSurName.text.toString()
            val genders = when (binding.radioGenders.checkedRadioButtonId) {
                binding.radioMale.id -> "Male"
                binding.radioFemale.id -> "Female"
                binding.radioOthers.id -> "Other"
                else -> "Not Selected" // In case no radio button is selected
            }

            // Sign up the user first
            authViewModel.signUp(email, password)

            // Observe the result of the sign-up process before creating the user
            authViewModel.signUpResult.observe(viewLifecycleOwner) { signUpResult ->
                if (signUpResult.isSuccess) {
                    // Sign-up successful, create the user
                    authViewModel.createUser(name, surName, email, genders)
                    Toast.makeText(requireContext(), "Sign-up successful, creating user...", Toast.LENGTH_SHORT).show()

                } else {
                    // Sign-up failed, show error message
                    Toast.makeText(requireContext(), "Sign-up failed: ${signUpResult.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }



        /*myRef.setValue("Hello!").addOnSuccessListener {
            Toast.makeText(requireContext(), "Sucess", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e->
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }*/

    }

}