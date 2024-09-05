package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentUserProfileBinding
import com.example.socialmediavbsanalay.presentation.adapters.UserAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        // Initialize Adapter
        userAdapter = UserAdapter()
        binding.searchRecyclerView.adapter = userAdapter
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe users LiveData
        userViewModel.usersList.observe(viewLifecycleOwner, Observer { users ->
            userAdapter.updateUsers(users)
        })

        // Load users
        userViewModel.fetchAllUsers()

        // Set up search functionality
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    val filteredUsers = userViewModel.usersList.value?.filter { user ->
                        user.name.contains(it, ignoreCase = true)
                    }
                    userAdapter.updateUsers(filteredUsers ?: emptyList())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return binding.root
    }

}
