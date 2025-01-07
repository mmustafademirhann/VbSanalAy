package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.socialmediavbsanalay.databinding.FragmentMessageBinding
import com.example.socialmediavbsanalay.presentation.adapters.MessageItemAdaptor
import com.example.socialmediavbsanalay.presentation.viewModels.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageItemAdaptor: MessageItemAdaptor
    private val messageViewModel: MessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatRoomId = arguments?.getString("chatRoomId")
        if (chatRoomId.isNullOrEmpty()) {
            Toast.makeText(context, "Chat room ID is missing", Toast.LENGTH_SHORT).show()
            return // Exit early if the chatRoomId is missing
        }

        messageItemAdaptor = MessageItemAdaptor()
        binding.messageItemRecyclerView.adapter = messageItemAdaptor

        lifecycleScope.launch {
            messageViewModel.loadMessages(chatRoomId)
        }

        messageViewModel.messages.observe(viewLifecycleOwner) { messageItems ->
            messageItemAdaptor.setMessageItems(messageItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
