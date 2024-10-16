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
    private var likedPostId = ""
    private var likedPostUserId = ""
    private var likedPostImage = ""
    private var lastClickedPostPosition = RecyclerView.NO_POSITION

    companion object {
        const val ARG_USER_ID = "userId"
        private const val ARG_POST_ID = "postId"
        fun newInstance(userId: String,postId:String): PostDetailFragment {
            return PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                    putString(ARG_POST_ID, postId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun navigateToUserProfile(username: String) {
        // FragmentTransaction başlat
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        var isFromSearch=true
        if (username== userPreferences.getUser()!!.id){
            isFromSearch=false
        }
        // Yeni UserProfileFragment'ı oluştur, kullanıcı adını parametre olarak geçir
        val userProfileFragment = UserProfileFragment.newInstance(username,isFromSearch)

        // Doğru fragment ile değiştir
        transaction.replace(R.id.fragmentContainerView, userProfileFragment)

        // İşlem tamamlandığında geriye dönülmemesini sağlamak için commit()
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getString(ARG_USER_ID) ?: ""

        // Adapter'ı başlat
        postAdapter = PostAdapter(
            userId,
            userViewModel,
            onCommentClick = { postId, postOwner, postImage ->
                showCommentBottomSheet(postId, postOwner, postImage)
            },
            onLikeClick = { postId, userId, postImage ->
                lastClickedPostPosition = getCurrentVisibleItemPosition()  // Beğeni tıklanırken pozisyonu kaydet
                userViewModel.likeOrUnLikePost(postId, userId, true)
            },
            onUnLikeClick = { postId, userId ->
                lastClickedPostPosition = getCurrentVisibleItemPosition()  // Beğeni tıklanırken pozisyonu kaydet
                userViewModel.likeOrUnLikePost(postId, userId, false)
            },
            onUsernameClick = { username ->
                navigateToUserProfile(username)
            }
        )

        // RecyclerView ayarlarını yap
        binding.postsRecyclerViewForDetails.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }

        // ViewModel'deki postları gözlemle
        galleryViewModel.posts.observe(viewLifecycleOwner) { posts ->
            val filteredPosts = posts.filter { post -> post.username == userId }

            // Adapter'ı güncelle
            postAdapter.setPosts(filteredPosts)

            // Eğer son tıklanan post varsa, o post'a scroll et
            if (lastClickedPostPosition != RecyclerView.NO_POSITION) {
                (binding.postsRecyclerViewForDetails.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastClickedPostPosition, 0)
            }
        }

        // Swipe-to-refresh işlemini başlat
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshGallery()
        }
        refreshGallery()
    }

    private fun getCurrentVisibleItemPosition(): Int {
        val layoutManager = binding.postsRecyclerViewForDetails.layoutManager as LinearLayoutManager
        return layoutManager.findFirstVisibleItemPosition()  // Görülen ilk post'un pozisyonunu al
    }
    private fun refreshGallery() {
        viewLifecycleOwner.lifecycleScope.launch {
            galleryViewModel.refreshGallery()
            binding.swipeRefreshLayout.isRefreshing = false // Refresh tamamlandığında animasyonu durdur
        }
    }
}
