package com.example.socialmediavbsanalay.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.databinding.FragmentSignInBinding
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    @Inject
    lateinit var userPreferences: UserPreferences

    private val hideHandler = Handler(Looper.myLooper()!!)
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()


    private var email: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view=binding.root
        return view


    }
    private fun onSignInSuccess() {
        // Kullanıcı başarılı giriş yaptıktan sonra, giriş durumunu kaydet
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_signed_in", true).apply()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomBar()
        binding.signInButton.setOnClickListener{
            email=binding.editTextMail.text.toString()
            val password=binding.editTextNumberPassword.text.toString()
            authViewModel.signIn(email,password)
        }
        authViewModel.authState.observe(viewLifecycleOwner){result->
            result.onSuccess { user ->
                onSignInSuccess()
                authViewModel.getUserByEmail(email)

            }.onFailure { exception ->
                if (exception.message == "User not found") {
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Sign-in failed: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        authViewModel.loggedUser.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                it.getOrNull()?.let {user ->
                    userPreferences.saveUser(user)
                    navigateToMainPage(view)
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
    private fun switchFragment(fragment: Fragment) {

    }
    fun navigateToSignUp(view: View){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, SignUpFragment())
            .commit()
    }
    fun navigateToMainPage(view: View){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, MainPageFragment())
            .commit()

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