package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentMainPageBinding
import com.example.socialmediavbsanalay.presentation.adapters.PostAdapter
import com.example.socialmediavbsanalay.presentation.adapters.StoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.domain.model.Comment
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [MainPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainPageFragment : Fragment() {

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter
    private val galleryViewModel: GalleryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    @Inject
    lateinit var userPreferences: UserPreferences


    private fun navigateToSearchResultsFragment() {
        val searchUserPostFragment = SearchUserPostFragment()
        replaceFragment(searchUserPostFragment)
    }


    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment) // Use your actual container ID
            .addToBackStack(null) // Optional, if you want back navigation
            .commit()
    }



    companion object {
        private const val GALLERY_REQUEST_CODE = 123
        //private val REQUEST_IMAGE_CAPTURE = 1
        //private val PICK_IMAGE = 2
        //private val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
    }
    private fun showCommentBottomSheet(postId: String) {
        val commentBottomSheetFragment = CommentBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putString("postId", postId) // postId'yi argüman olarak gönder
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        postAdapter = PostAdapter(userViewModel,
            onCommentClick = { postId ->
                showCommentBottomSheet(postId) // Yorum ikonuna tıkladığında Bottom Sheet'i aç
            },
            onLikeClick = { postId, userId -> // Beğeni tıklama olayı
                // Burada kullanıcının beğeni işlemine göre ilgili ViewModel fonksiyonunu çağır
                userViewModel.likePost(postId, userId) // UserID'yi doğru bir şekilde sağladığınızdan emin olun
            }
        )


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).showBottomBar()
        binding.progressBar.visibility = View.VISIBLE
        galleryViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.setPosts(posts)
            binding.progressBar.visibility = View.INVISIBLE
        }

        val recyclerView = binding.postsRecyclerView
        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.adapter=postAdapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                galleryViewModel.refreshGallery()
                binding.swipeRefreshLayout.isRefreshing = false // Refresh tamamlandığında animasyonu durdurun
            }
        }

        adapterFunctions()
        // Load your stories into the adapter
        binding.editTextText3.setOnClickListener {
            navigateToSearchResultsFragment()
        }


    }
    private fun navigateToSame(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
    private fun loadStories(){
        userViewModel.stories.observe(viewLifecycleOwner) { stories ->
            // Update the adapter with the fetched stories
            storyAdapter.setStories(stories)
        }

        // Fetch stories from the ViewModel
        userViewModel.fetchStories() // This will trigger the fetch process

    }

    private fun onStoryClicked(position: Int) {
        // Handle the logic when a story is clicked
        // For example, navigate to a story detail fragment
        Toast.makeText(requireContext(), "Story clicked: $position", Toast.LENGTH_SHORT).show()
    }

    private fun openUploadStoryDialog() {
        // Implement the logic to open an upload story dialog or activity
        Toast.makeText(requireContext(), "Upload Story clicked", Toast.LENGTH_SHORT).show()
    }

    private fun adapterFunctions() {
        val currentUserId = userPreferences.getUser()?.id.toString() // Get the current user's ID

        // Initialize the StoryAdapter with the necessary parameters
        storyAdapter = StoryAdapter(
            stories = emptyList(), // Load your list of stories
            currentUser = currentUserId,
            onStoryClick = { position ->
                // Handle the story click event (e.g., open story detail)
                onStoryClicked(position)
            },
            onUploadStoryClick = {
                // Handle the upload story action (e.g., open upload dialog)
                openUploadStoryDialog()
            }
        )
        loadStories()
        binding.storyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = storyAdapter
        }
        postAdapter = PostAdapter(userViewModel,
            onCommentClick = { postId ->
                showCommentBottomSheet(postId) // Yorum ikonuna tıkladığında Bottom Sheet'i aç
            },
            onLikeClick = { postId, userId -> // Beğeni tıklama olayı
                // Burada kullanıcının beğeni işlemine göre ilgili ViewModel fonksiyonunu çağır
                userViewModel.likePost(postId, userId) // UserID'yi doğru bir şekilde sağladığınızdan emin olun
            }
        )

        binding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = postAdapter
        }
        userViewModel.stories.observe(viewLifecycleOwner) { stories ->
            storyAdapter.setStories(stories) // Update the adapter with the fetched stories
        }

        // Fetch the stories
        userViewModel.fetchStories() // Trigger the fetch process

    }


    // ContextCompat.checkSelfPermission(
    // requireContext(),


}