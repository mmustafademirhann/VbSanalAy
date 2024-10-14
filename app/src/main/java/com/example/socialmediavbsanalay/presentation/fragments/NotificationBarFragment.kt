package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.databinding.FragmentNotificationBarBinding
import com.example.socialmediavbsanalay.presentation.adapters.NotificationsAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NotificationBarFragment : Fragment() {

    private val notificationViewModel: NotificationViewModel by viewModels()

    private var _binding: FragmentNotificationBarBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userPreferences: UserPreferences

    private lateinit var notificationsAdapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationsAdapter = NotificationsAdapter()
        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = notificationsAdapter
        }
        notificationViewModel.getNotifications(userPreferences.getUser()?.id ?: "")
        initObservables()
    }

    private fun initObservables() {
        notificationViewModel.getNotificationLiveData.observe(viewLifecycleOwner) {
            if (it is ApiResponse.Loading) {
                binding.progressBar.visibility = View.VISIBLE
            }
            if (it is ApiResponse.Success) {
                binding.progressBar.visibility = View.GONE
                if (it.data.isEmpty()) {
                    binding.tvEmptyNotification.visibility = View.VISIBLE
                } else {
                    binding.tvEmptyNotification.visibility = View.GONE
                    notificationsAdapter.setNotifications(it.data)
                }
            }
            if(it is ApiResponse.Fail) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), it.e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}