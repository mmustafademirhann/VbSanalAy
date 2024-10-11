package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentUserFollowersBinding
import com.example.socialmediavbsanalay.presentation.adapters.UserAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
class UserFollowersFragment : Fragment() {

    private lateinit var binding: FragmentUserFollowersBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var targetUserId: String // targetUserId değişkenini tanımlayın
    private var isFromFollowing:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Arguments'ten targetUserId değerini al
        arguments?.let {
            targetUserId = it.getString("currentUserId") ?: "" // Hedef kullanıcı ID'sini alın
            //val isFromSearch = it.getBoolean("isFromSearch", true)
            isFromFollowing = arguments?.getBoolean("isFromFollowing", false) ?: false
        }

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Adapter'ı oluştur
        adapter = UserAdapter { userId -> onUserClicked(userId) }


        // Adapter'ı RecyclerView'e ata
        binding.recyclerView.adapter = adapter

        // Takipçi listesini gözlemle

        // Takipçi listesini yükle

        // Takipçi listesini gözlemle
        if (isFromFollowing) {
            // Takip edilenleri yükle
            userViewModel.loadFollowing(targetUserId)
            userViewModel.following.observe(viewLifecycleOwner) { followingList ->
                adapter.updateUsers(followingList)
            }
        } else {
            // Takipçileri yükle
            userViewModel.loadFollowers(targetUserId)
            userViewModel.followers.observe(viewLifecycleOwner) { followerList ->
                adapter.updateUsers(followerList)
            }
        }
    }

    private fun onUserClicked(userId: String) {
        val isFromSearch = true // Search'ten geldiğini belirtiyoruz
        val userProfileFragment = UserProfileFragment.newInstance(userId, isFromSearch)

        Log.d("Navigation", "Navigating to user profile with ID: $userId")

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, userProfileFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun navigateToUserProfile(userId: String) {
        val isFromSearch = true // Search'ten geldiğini belirtiyoruz
        val userProfileFragment = UserProfileFragment.newInstance(userId, isFromSearch)
        val args = Bundle().apply {
            putString("userId", userId) // Kullanıcı ID'sini gönder
        }
        userProfileFragment.arguments = args

        // Perform navigation
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, userProfileFragment)
            .addToBackStack(null) // Geri gitmek istersen back stack'e ekle
            .commit()
    }

}
