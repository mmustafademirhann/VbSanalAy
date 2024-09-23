package com.example.socialmediavbsanalay.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentSearchUserPostBinding
import com.example.socialmediavbsanalay.presentation.adapters.UserAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchUserPostFragment : Fragment() {

    private lateinit var binding: FragmentSearchUserPostBinding
    private val userViewModel: UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchUserPostBinding.inflate(inflater, container, false)

        // Initialize RecyclerView and Adapter with click listener
        val userAdapter = UserAdapter { userId ->
            navigateToUserProfile(userId) // Handle item click
        }
        binding.searchRecyclerViewView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Observe users StateFlow
        lifecycleScope.launchWhenStarted {
            userViewModel.users.collect { userList ->
                userAdapter.updateUsers(userList)
            }
        }

        // Load all users initially
        userViewModel.fetchAllUsers()

        // Set up search functionality
        binding.editTextTextSearc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    userViewModel.searchUsers(it.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showEditTextWithKeyboard()
        // Use binding to access views
        // Example: binding.someTextView.text = "Hello"
    }

    private fun navigateToUserProfile(userId: String) {
        val isFromSearch = true // Search'ten geldiğini belirtiyoruz
        val userProfileFragment = UserProfileFragment.newInstance(userId, isFromSearch)

        // Perform navigation
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, userProfileFragment)
            .addToBackStack(null) // Geri gitmek istersen back stack'e ekle
            .commit()
    }


    private fun showEditTextWithKeyboard() {
        // Request focus for the EditText
        binding.editTextTextSearc.requestFocus()

        // Show the keyboard
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editTextTextSearc, InputMethodManager.SHOW_IMPLICIT)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchUserPostFragment().apply {
                // Set arguments if needed
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }
}
