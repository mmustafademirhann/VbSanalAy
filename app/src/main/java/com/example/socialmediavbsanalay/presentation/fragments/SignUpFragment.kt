package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentSignUpBinding
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.viewModels.AuthViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val database = FirebaseDatabase.getInstance().reference


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
        binding.btnRegister.setOnClickListener {
            saveUserData()
        }
        return view
    }
    private fun handleSignUp() {
        val email = binding.etMail.text.toString()
        val password = binding.editTextNumberPassword2.text.toString()
        val name = binding.etName.text.toString()
        val surname = binding.etSurName.text.toString()
        val gender = when (binding.radioGenders.checkedRadioButtonId) {
            R.id.radio_male -> "MALE"
            R.id.radio_female -> "FEMALE"
            else -> ""
        }

        authViewModel.signUp(email, password, name, surname, gender)
    }
    private fun saveUserData() {
        // Collect data from EditTexts and RadioGroup
        val name = binding.etName.text.toString().trim()
        val surName=binding.etSurName.text.toString().trim()
        val email = binding.etMail.text.toString().trim()
        val selectedGenderId = binding.radioGenders.checkedRadioButtonId
        val gender = when (selectedGenderId) {
            R.id.radio_male -> "Male"
            R.id.radio_female -> "Female"
            else -> ""
        }

        // Input validation
        if (name.isEmpty() || email.isEmpty() || surName.isEmpty() ||  gender.isEmpty()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Save data to Firebase
        val userId = database.child("users").push().key
        val user = User(name,surName, email, gender)

        userId?.let {
            database.child("users").child(it).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(context, "User data saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed to save user data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomBar()
        authViewModel.signUpResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { firebaseUser ->
                    firebaseUser?.let { user ->
                        val newUser = User(
                            id = user.uid,
                            name = binding.etName.text.toString(),
                            surName = binding.etSurName.text.toString(),
                            email = binding.etMail.text.toString(),
                            gender = binding.radioGenders.checkedRadioButtonId.toString()
                        )
                        userViewModel.addUser(newUser)
                    } ?: run {
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
                email = binding.etMail.text.toString(),
                password = binding.editTextNumberPassword2.text.toString(),
                name = binding.etName.text.toString(),   // Ensure these fields exist in your layout
                surname = binding.etSurName.text.toString(),
                gender = binding.radioGenders.checkedRadioButtonId.toString()
            )
           //handleSignUp()
        }
    }

}