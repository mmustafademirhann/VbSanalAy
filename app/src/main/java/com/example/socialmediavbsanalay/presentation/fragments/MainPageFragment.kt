package com.example.socialmediavbsanalay.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentMainPageBinding
import com.example.socialmediavbsanalay.presentation.adapters.PostAdapter
import com.example.socialmediavbsanalay.presentation.adapters.StoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.data.repository.ApiResponse
import com.example.socialmediavbsanalay.domain.model.Comment
import com.example.socialmediavbsanalay.domain.model.Notification
import com.example.socialmediavbsanalay.domain.model.NotificationType
import com.example.socialmediavbsanalay.domain.model.Story
import com.example.socialmediavbsanalay.domain.model.UserStories
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.story.StoryActivity
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.NotificationViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.protobuf.Api
import kotlinx.coroutines.launch
import javax.inject.Inject



@AndroidEntryPoint
class MainPageFragment : Fragment() {

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var storyActivityLauncher: ActivityResultLauncher<Intent>
    private var likedPostId = ""
    private var likedPostUserId = ""
    private var likedPostImage = ""
    private lateinit var textNoPostsMessage: TextView

    @Inject
    lateinit var userPreferences: UserPreferences
    //gitegittimi

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        postAdapter = PostAdapter(
            userPreferences.getUser()?.id ?: "",
            userViewModel,
            onCommentClick = { postId, postOwner, postImage ->
                showCommentBottomSheet(postId, postOwner, postImage) // Yorum ikonuna tıkladığında Bottom Sheet'i aç
            },
            onLikeClick = { postId, userId, postImage -> // Beğeni tıklama olayı
                // Burada kullanıcının beğeni işlemine göre ilgili ViewModel fonksiyonunu çağır
                likedPostId = postId
                likedPostUserId = userId
                likedPostImage = postImage
                userViewModel.likeOrUnLikePost(postId, userId, true) // UserID'yi doğru bir şekilde sağladığınızdan emin olun
            },
            onUnLikeClick = {postId, userId ->
                userViewModel.likeOrUnLikePost(postId, userId, false)
            }
        )


        return binding.root
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as MainActivity).showBottomBar()
        binding.progressBar.visibility = View.VISIBLE
        galleryViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.setPosts(posts)
            binding.progressBar.visibility = View.INVISIBLE
        }

        userViewModel.likeSuccess.observe(viewLifecycleOwner) {
            if (it) {
                notificationViewModel.addNotification(
                    Notification(
                        likedPostUserId,
                        userPreferences.getUser()?.profileImageUrl ?: "",
                        userPreferences.getUser()?.id ?: "",
                        NotificationType.LIKE.notificationType,
                        likedPostId,
                        likedPostImage,
                        false
                    )
                )
            }
        }

        val recyclerView = binding.postsRecyclerView
        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.adapter=postAdapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                galleryViewModel.refreshPostsAfterFollow()
                binding.swipeRefreshLayout.isRefreshing = false // Refresh tamamlandığında animasyonu durdurun
                galleryViewModel.loadPosts()
            }
        }

        textNoPostsMessage = view.findViewById(R.id.textNoPostsMessage)

        // Posts'u yükleyelim
        galleryViewModel.loadPosts()

        // noPostsMessage LiveData'yı gözlemle
        galleryViewModel.noPostsMessage.observe(viewLifecycleOwner, Observer { show ->
            if (show) {
                textNoPostsMessage.visibility = View.VISIBLE // Mesajı göster
            } else {
                textNoPostsMessage.visibility = View.GONE // Mesajı gizle
            }
        })

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery() // Eğer izin verilmişse galeriyi aç
            } else {
                // İzin verilmediyse yapılacak işlemler
            }
        }

        // Galeriden resim seçme işleyicisi
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Kullanıcıdan açıklama al
                    val description = "Hikaye açıklamanız" // Burayı kullanıcının girdiği açıklama ile değiştirebilirsiniz
                    val ownerUser = userPreferences.getUser()?.id // Mevcut kullanıcı bilgisi

                    // Story nesnesini oluştur
                    val story = Story(
                        id=id.toString(),
                        imageUrl = uri.toString(), // veya başka bir URL alabilirsiniz, eğer resmi yüklüyorsanız
                        ownerUser = ownerUser.toString(),
                        description = description,
                        timestamp = System.currentTimeMillis(),
                        storyExpireTime = (System.currentTimeMillis() + 86400000)
                    )

                    // Seçilen resmi ve diğer bilgileri ViewModel aracılığıyla yükle
                    userViewModel.uploadStory(story)
                }
            }
        }

        storyActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                userViewModel.fetchUserStories()
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
            //storyAdapter.setStories(stories)
        }

        // Fetch stories from the ViewModel
        userViewModel.fetchStories() // This will trigger the fetch process

    }

    private fun onStoryClicked(isUploadOperation: Boolean, userStories: UserStories?, adapterPosition: Int, storyPosition: Int?) {
        if (isUploadOperation) {
            openGallery()
        } else {
            val intent = StoryActivity.start(requireContext(), storyAdapter.getStories(), adapterPosition, storyPosition ?: 0)
            storyActivityLauncher.launch(intent)
        }
    }

    private fun openUploadStoryDialog() {
        // Implement the logic to open an upload story dialog or activity
        Toast.makeText(requireContext(), "Upload Story clicked", Toast.LENGTH_SHORT).show()
    }
    fun List<UserStories>.toStoryList(): List<Story> {
        return this.map { userStory ->
            Story(

                id=id.toString(),
                ownerUser = userStory.ownerUser,
                // Diğer Story özelliklerini burada doldurun
            )
        }
    }

    private fun adapterFunctions() {
        val currentUserId = userPreferences.getUser()?.id.toString()

        // Kullanıcı hikayelerini ve tüm kullanıcıları yükle
        userViewModel.loadUsersWithStories() // Kullanıcı hikayelerini yükle
        userViewModel.fetchUserStories() // Mevcut kullanıcının hikayelerini yükle
        userViewModel.fetchAllUsersExcludingCurrentUser() // Diğer kullanıcıları yükle

        // Kullanıcı hikayelerini gözlemle
        userViewModel.usersWithStories.observe(viewLifecycleOwner) { users ->
            // Kullanıcıları hikayelerine dönüştürüp adapter'a aktar
            val stories = users.flatMap { user ->
                user.stories.map { story ->
                    Story(id = story.id, ownerUser = user.id, imageUrl = story.imageUrl)
                }
            }
            // Hikaye listesini adapter'a aktar
            //storyAdapter.setStories(stories)
        }

        // Kullanıcı hikayelerini gözlemleyin
        userViewModel.userStories.observe(viewLifecycleOwner) { userStories ->
            if (userStories is ApiResponse.Success) {
                // UserStories listesini Story listesine dönüştür
                // Hikaye listesini adapter'a aktar
                // StoryAdapter'ı başlat
                binding.progressBar.visibility = View.GONE
                val userArrayList = userStories.data
                if (userArrayList.filter { it.ownerUser == currentUserId }.isEmpty()) {
                    userArrayList.add(
                        UserStories(
                            userPreferences.getUser()?.id ?: "",
                            emptyList(),
                            userPreferences.getUser()?.profileImageUrl ?: ""
                        )
                    )
                }
                run loop@{
                    userArrayList.forEachIndexed { index, userStory ->
                        if (userStory.ownerUser == currentUserId && index != 0) {
                            val item = userArrayList.removeAt(index)
                            // Insert the item at the first position (index 0)
                            userArrayList.add(0, item)
                            // Break the loop since we only need to move the first matching item
                            return@loop
                        }
                    }
                }
                storyAdapter = StoryAdapter(
                    userStories = userArrayList as ArrayList<UserStories>,
                    currentUser = currentUserId,
                    userViewModel,
                    onStoryClick = { isUploadOperation, userStories, adapterPosition, storyPosition ->
                        onStoryClicked(
                            isUploadOperation,
                            userStories,
                            adapterPosition,
                            storyPosition
                        )
                    }
                )

                binding.storyRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = storyAdapter
                }
            }
            if(userStories is ApiResponse.Loading) {
                binding.progressBar.visibility = View.VISIBLE
            }
            if (userStories is ApiResponse.Fail) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Bilinmeyen bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.uploadStoryLiveData.observe(viewLifecycleOwner) {
            if (it is ApiResponse.Success) {
                Toast.makeText(requireContext(), "Story başarıyla yüklendi.", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                userViewModel.fetchUserStories()
            }
            if (it is ApiResponse.Loading) {
                binding.progressBar.visibility = View.VISIBLE
            }
            if (it is ApiResponse.Fail) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Bilinmeyen bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show()
            }
        }
    }





    // ContextCompat.checkSelfPermission(
    // requireContext(),


}