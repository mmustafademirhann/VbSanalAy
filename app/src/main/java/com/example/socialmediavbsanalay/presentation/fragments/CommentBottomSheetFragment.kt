package com.example.socialmediavbsanalay.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.databinding.BottomSheetCommentBinding
import com.example.socialmediavbsanalay.domain.model.Comment
import com.example.socialmediavbsanalay.presentation.adapters.CommentAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class CommentBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetCommentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postId = arguments?.getString("postId") ?: return

        // Setup RecyclerView
        setupRecyclerView()

        // Yorumları fetchComments ile yükle
        viewModel.fetchComments(postId)

        // Yorumları gözlemleyin
        viewModel.comments.observe(viewLifecycleOwner) { commentsList ->
            Log.d("CommentBottomSheet", "Loaded comments: $commentsList")
            commentAdapter.submitList(commentsList)
        }

        // Send button click listener
        binding.buttonSendComment.setOnClickListener {
            val commentText = binding.editTextComment.text.toString().trim()
            if (commentText.isNotEmpty()) {
                // Create new comment
                val newComment = Comment(
                    commentId = UUID.randomUUID().toString(),
                    postId = postId,
                    userId = userPreferences.getUser()!!.id,
                    comment = commentText,
                    username = userPreferences.getUser()!!.name,
                    profileImageUrl = userPreferences.getUser()!!.profileImageUrl,
                    timestamp = System.currentTimeMillis()
                )

                // Yorum ekle
                viewModel.addComment(newComment)
                binding.editTextComment.text.clear()
            } else {
                Toast.makeText(requireContext(), "Lütfen bir yorum girin.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // Dialog arka planını transparan köşeli hale getir
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheet = (dialogInterface as BottomSheetDialog).findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            ) as FrameLayout?
            bottomSheet?.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_bottom_sheet)


        }


        return dialog
    }

    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter() // Initialize adapter without arguments
        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.commentsRecyclerView.adapter = commentAdapter
    }

    private fun loadComments(postId: String) {
        lifecycleScope.launch {
            viewModel.loadComments(postId).collect { commentsList ->
                Log.d("CommentBottomSheet", "Loaded comments: $commentsList")
                commentAdapter.submitList(commentsList) // Submit the loaded comments
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference
    }
}
