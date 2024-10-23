package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.databinding.FragmentPostDetailBinding
import com.example.socialmediavbsanalay.domain.model.Comment
import com.example.socialmediavbsanalay.presentation.adapters.PostAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailBinding
    private val galleryViewModel: GalleryViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter
    private val userViewModel: UserViewModel by viewModels()

    @Inject
    lateinit var userPreferences: UserPreferences
    private var lastScrollPosition = RecyclerView.NO_POSITION
    private lateinit var postId: String

    companion object {
        const val ARG_USER_ID = "userId"
        private const val ARG_POST_ID = "postId"
        private const val ARG_POSITION = "position"

        fun newInstance(userId: String, postId: String, position: Int): PostDetailFragment {
            return PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                    putString(ARG_POST_ID, postId)
                    putInt(ARG_POSITION, position)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)

        val userId = arguments?.getString(ARG_USER_ID) ?: ""
        postId = arguments?.getString(ARG_POST_ID) ?: ""
        val position = arguments?.getInt(ARG_POSITION, RecyclerView.NO_POSITION) ?: RecyclerView.NO_POSITION

        setupRecyclerView()
        observePosts(userId)

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshGallery()
        }

        refreshGallery()
        return binding.root
    }

    private fun setupRecyclerView() {
        // ARG_USER_ID'den userId alıyoruz (currentUserId)
        val currentUserId = arguments?.getString(ARG_USER_ID) ?: ""

        // ARG_POST_ID'den postId alıyoruz
        postId = arguments?.getString(ARG_POST_ID) ?: ""

        // PostAdapter'ı oluşturuyoruz
        postAdapter = PostAdapter(
            currentUserId = currentUserId,  // currentUserId'yi buraya geçiriyoruz
            postId = postId,                // postId'yi de geçiyoruz
            userViewModel = userViewModel,  // userViewModel adaptöre iletiliyor
            onCommentClick = { postId, postOwner, postImage ->
                // Yorum tıklamasında yapılacak işlemler
                showCommentBottomSheet(postId, postOwner, postImage)
            },
            onLikeClick = { postId, userId, postImage ->
                // Beğeni tıklamasında yapılacak işlemler
                userViewModel.likeOrUnLikePost(postId, userId, true)
            },
            onUnLikeClick = { postId, userId ->
                // Beğeni geri alma tıklamasında yapılacak işlemler
                userViewModel.likeOrUnLikePost(postId, userId, false)
            },
            onUsernameClick = { username ->
                // Kullanıcı adına tıklanınca yapılacak işlemler
                navigateToUserProfile(username)
            },
            lifecycleOwner = viewLifecycleOwner  // LifecycleOwner'ı geçiriyoruz
        )

        // RecyclerView ayarları
        binding.postsRecyclerViewForDetails.apply {
            layoutManager = LinearLayoutManager(context)  // LayoutManager ayarlanıyor
            adapter = postAdapter                         // Adapter bağlanıyor
        }
    }


    private var isInitialLoad = true // İlk yükleme durumu kontrolü için

    private fun observePosts(userId: String) {
        galleryViewModel.posts.observe(viewLifecycleOwner) { posts ->
            val filteredPosts = posts.filter { it.username == userId }
            postAdapter.setPosts(filteredPosts)

            // Belirtilen postId'yi bul
            val initialPosition = filteredPosts.indexOfFirst { it.id == postId }

            // Sadece ilk yüklemede kaydırma yap
            if (isInitialLoad && initialPosition != RecyclerView.NO_POSITION) {
                (binding.postsRecyclerViewForDetails.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(initialPosition, 0)
                isInitialLoad = false // İlk yükleme tamamlandı
            }
        }
    }



    private fun refreshGallery() {
        viewLifecycleOwner.lifecycleScope.launch {
            galleryViewModel.refreshGallery()
            binding.swipeRefreshLayout.isRefreshing = false // Refresh tamamlandığında animasyonu durdur
        }
    }

    private fun navigateToUserProfile(username: String) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        var isFromSearch = true
        if (username == userPreferences.getUser()?.id) {
            isFromSearch = false
        }

        val userProfileFragment = UserProfileFragment.newInstance(username, isFromSearch)

        transaction.replace(R.id.fragmentContainerView, userProfileFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showCommentBottomSheet(postId: String, postOwner: String, postImage: String) {
        val commentBottomSheetFragment = CommentBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putString("postId", postId) // postId'yi argüman olarak gönder,
                putString("postOwner", postOwner)
                putString("postImage", postImage)
            }
        }
        // Bottom Sheet'i oluştur
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_comment, null)
        val commentEditText = bottomSheetView.findViewById<EditText>(R.id.editTextComment)
        val submitButton = bottomSheetView.findViewById<Button>(R.id.buttonSendComment)

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)
        commentBottomSheetFragment.show(childFragmentManager, commentBottomSheetFragment.tag)
        submitButton.setOnClickListener {
            val commentText = commentEditText.text.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid // Kullanıcı ID'sini alın
            val userName = userPreferences.getUser()?.id.toString()
            val profilePhoto= userPreferences.getUser()?.profileImageUrl

            if (userId != null && commentText.isNotBlank()) {
                // Comment nesnesini oluştur
                val comment = Comment(
                    userId = userId, // Kullanıcı ID'sini ayarla
                    postId = postId, // Post ID'sini ayarla
                    comment = commentText, // Yorum metnini ayarla
                    username = userName,
                    profileImageUrl= profilePhoto.toString()
                )

                galleryViewModel.addComment(comment) // Yorum ekle
                bottomSheetDialog.dismiss() // Dialog'u kapat
            } else {
                Toast.makeText(requireContext(), "Yorum boş olamaz!", Toast.LENGTH_SHORT).show()
            }
        }


    }

}

