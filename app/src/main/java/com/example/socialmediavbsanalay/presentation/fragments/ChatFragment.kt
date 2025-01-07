package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.databinding.FragmentChatBinding
import com.example.socialmediavbsanalay.domain.model.MessageItem
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.adapters.MessageAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MessageViewModel by viewModels()
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun scrollToBottom() {
        binding.recyclerView.post {
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomBar()

        val chatRoomId = arguments?.getString("chatRoomId") ?: ""
        val currentUserId = arguments?.getString("currentUserId") ?: ""

        // RecyclerView ve Adapter kurulumu
        adapter = MessageAdapter(currentUserId)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()) // LayoutManager ekledik
        binding.recyclerView.adapter = adapter

        // İlk yükleme: Tüm mesajları bir defa çekiyoruz
        viewModel.loadMessages(chatRoomId)
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
                adapter.submitList(messages) {
                    scrollToBottom() // Yeni mesaj geldikçe kaydır
                }

        }

        // Anlık mesajları dinleme
        viewModel.listenToMessages(chatRoomId)

        // Mesaj gönderme işlemi
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()

            if (messageText.isNotEmpty()) {
                val targetUserId = arguments?.getString("targetUserId") ?: return@setOnClickListener
                val message = MessageItem(
                    senderId = currentUserId,
                    receiverId = targetUserId,
                    message = messageText
                )

                // Mesaj gönderme işlemi
                viewModel.sendMessage(chatRoomId, message)

                // Mesaj girişini temizle
                binding.messageInput.text.clear()

                // RecyclerView'i en alta kaydır
                binding.recyclerView.post {
                    binding.recyclerView.smoothScrollToPosition(binding.recyclerView.adapter?.itemCount?.minus(1) ?: 0)
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
