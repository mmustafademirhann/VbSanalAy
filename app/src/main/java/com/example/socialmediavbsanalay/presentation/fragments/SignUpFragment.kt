package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.databinding.FragmentSignUpBinding
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.viewModels.AuthViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    @Inject
    lateinit var userPreferences: UserPreferences

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database("https://sanalay-b69cd-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("message")

    var id: String = ""
    var email: String = ""
    var password: String = ""
    var name: String = ""
    var surName: String = ""
    var genders: String = ""

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
        val usersRef = Firebase.firestore.collection("user")
        binding.btnRegister.setOnClickListener {
            id = binding.userName.text.toString()
            email = binding.etMail.text.toString()
            password = binding.editTextNumberPassword2.text.toString()
            name = binding.etName.text.toString()
            surName = binding.etSurName.text.toString()
            genders = when (binding.radioGenders.checkedRadioButtonId) {
                binding.radioMale.id -> "Male"
                binding.radioFemale.id -> "Female"
                binding.radioOthers.id -> "Other"
                else -> "Not Selected" // In case no radio button is selected
            }
            usersRef.whereEqualTo("id", id)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // Aynı id ile kullanıcı bulundu, hata mesajı göster
                        Toast.makeText(requireContext(), "User ID already exists. Please choose a different ID.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Aynı ID yok, kullanıcı kaydı yapılabilir
                        authViewModel.signUp(email, password)
                        // Sign-up işlemini gözlemleyin
                        authViewModel.signUpResult.observe(viewLifecycleOwner) { signUpResult ->
                            if (signUpResult.isSuccess) {
                                userPreferences
                                // Sign-up başarılı, kullanıcı oluşturuluyor
                                authViewModel.createUser(id, name, surName, email, genders)

                                Toast.makeText(requireContext(), "Sign-up successful, creating user...", Toast.LENGTH_SHORT).show()

                            } else {
                                // Sign-up başarısız, hata mesajı göster
                                Toast.makeText(requireContext(), "Sign-up failed: ${signUpResult.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error checking user ID: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

            // Sign up the user first


        }

        authViewModel.createUserLiveData.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                userPreferences.saveUser(
                    User(
                        id,
                        name,
                        surName,
                        email,
                        genders
                    )
                )
            }
        }


        /*myRef.setValue("Hello!").addOnSuccessListener {
            Toast.makeText(requireContext(), "Sucess", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e->
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }*/

    }

}