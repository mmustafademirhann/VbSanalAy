package com.example.socialmediavbsanalay.presentation.fragments
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.databinding.FragmentUserProfileBinding
import com.example.socialmediavbsanalay.domain.model.Notification
import com.example.socialmediavbsanalay.domain.model.NotificationType
import com.example.socialmediavbsanalay.domain.model.Post
import com.example.socialmediavbsanalay.domain.model.User
import com.example.socialmediavbsanalay.presentation.OnItemClickListener
import com.example.socialmediavbsanalay.presentation.adapters.UserAdapter
import com.example.socialmediavbsanalay.presentation.adapters.UserPostAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.NotificationViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile), OnItemClickListener {

    @Inject
    lateinit var userPreferences: UserPreferences

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    private lateinit var userPostAdapter: UserPostAdapter
    private lateinit var binding: FragmentUserProfileBinding
    private val galleryViewModel: GalleryViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()
    private lateinit var userId: String
    private lateinit var ownerUser:String
    var x=""
    private lateinit var currentUserId: String
    private lateinit var targetUserId: String // Hedef kullanıcının ID'si

    private lateinit var followersCountTextView: TextView
    private lateinit var followingCountTextView: TextView
    private lateinit var followButton: Button



    //private var isOwner: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)


        // TextView'leri bul
        followersCountTextView = binding.folowersTextView
        followingCountTextView = binding.folowingTextView




        // Giriş yapmış kullanıcının ID'sini al
        // (Kullanıcı bilgilerini UserPreferences'tan alabilirsiniz)
        currentUserId = userPreferences.getUser()?.id ?: return requireView()


        // Hedef kullanıcıyı ayarla
        // targetUserId, kullanıcı arayüzünden seçilebilir veya başka bir kaynaktan alınabilir.



        // Initialize UserAdapter with onItemClick lambda
        userAdapter = UserAdapter { userId ->
            if (userId == userPreferences.getUser()!!.id) {
                // Eğer userId eşitse, yeni fragment'a is_from_search parametresi ile false geç
                val userDetailFragment = UserProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_USER_ID, userId)  // userId'yi gönder
                        putBoolean(ARG_IS_FROM_SEARCH, false)  // is_from_search'i false yap
                    }
                }

                // Fragment'e geçiş yap
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, userDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        // Initialize RecyclerView and Adapter
        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter

        }

        // Observe user list and update the adapter
        lifecycleScope.launch {
            userViewModel.users.collect { userList ->
                userAdapter.updateUsers(userList)

            }
        }

        // Load all users
        userViewModel.fetchAllUsers()
        followButton = binding.followButton






        return binding.root
    }

    companion object {
        private const val ARG_USER_ID = "userId"
        private const val ARG_IS_FROM_SEARCH = "is_from_search"
        fun newInstance(userId: String): PostDetailFragment {
            val fragment = PostDetailFragment()
            val args = Bundle().apply {
                putString(ARG_USER_ID, userId)

            }
            fragment.arguments = args
            return fragment
        }//?

        fun newInstance(userId: String, isFromSearch: Boolean): UserProfileFragment {
            val fragment = UserProfileFragment()
            val args = Bundle().apply {
                putString(ARG_USER_ID, userId)
                putBoolean(ARG_IS_FROM_SEARCH, isFromSearch)
            }
            fragment.arguments = args
            return fragment
        }
    }
    private fun navigateToUserFollowing(userId: String) {
        val userFollowingFragment = UserFollowersFragment().apply {
            arguments = Bundle().apply {
                putString("currentUserId", userId)
                putBoolean("isFromSearch",true)
                putBoolean("isFromFollowing", true) // Takip edilenler sayfası
            }
        }

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_center,
                R.anim.exit_to_center,
                R.anim.enter_from_left_center,
                R.anim.exit_to_right_center
            )
            .replace(R.id.fragmentContainerView, userFollowingFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToUserFollowers(userId: String) {
        val userFollowersFragment = UserFollowersFragment().apply {
            arguments = Bundle().apply {
                putString("currentUserId", userId) // Kullanıcı ID'sini geçir
                putBoolean("isFromSearch",true)
                putBoolean("isFromFollowing", false)
            }
        }

        // Fragment geçişini başlat
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_center,
                R.anim.exit_to_center,
                R.anim.enter_from_left_center,
                R.anim.exit_to_right_center
            ) // Yeni animasyonları kullan
            .replace(R.id.fragmentContainerView, userFollowersFragment)
            .addToBackStack(null) // Geri butonuyla dönmek için ekle
            .commit()
    }
    // Filtreleme işlemi, userId kullanarak postları filtreliyoruz
    private fun observePosts(userId: String) {

        // Postları userId'ye göre filtreleme işlemi
        galleryViewModel.posts.observe(viewLifecycleOwner) { postsForUsers ->
            val filteredPosts = postsForUsers.filter { post -> post.username == userId }
            userPostAdapter.setPosts(filteredPosts)
        }

        // Kullanıcı verilerini gözlemleme işlemi
        galleryViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Kullanıcı bulundu, UI'yi güncelle



                binding.usernameM.text = it.id
                targetUserId=it.id
                binding.userHandle.text = "@${it.name}" // Kullanıcının handle'ı
                //updateFollowerAndFollowingCounts()
                Glide.with(this)
                    .load(it.profileImageUrl) // Kullanıcının profil resim URL'si
                    .placeholder(R.drawable.add) // Yükleme sırasında gösterilecek varsayılan resim
                    .error(R.drawable.sayfabitti) // Hata durumunda gösterilecek resim
                    .circleCrop() // Resmi yuvarlak yapar
                    .into(binding.profileImageP)
                Glide.with(this)
                    .load(it.profileBacgroundImageUrl) // Kullanıcının profil resim URL'si
                    .placeholder(R.drawable.rainy_minecraft) // Yükleme sırasında gösterilecek varsayılan resim
                    .error(R.drawable.rainy_minecraft) // Hata durumunda gösterilecek resim
                    .into(binding.mainBackgroundImage)
                userViewModel.loadFollowerCount(targetUserId)
                userViewModel.loadFollowingCount(targetUserId)
                userViewModel.checkIfUserIsFollowing(currentUserId, targetUserId)

                // Takipçi sayısını gözlemle
                userViewModel.followerCount.observe(viewLifecycleOwner) { count ->
                    binding.folowersTextView.text = count.toString()
                }
                userViewModel.followingCount.observe(viewLifecycleOwner) { count ->
                    binding.folowingTextView.text = count.toString() // UI'da göstermek için
                }
                // Takip durumunu gözlemle ve butonun metnini güncelle
                userViewModel.isFollowing.observe(viewLifecycleOwner) { isFollowing ->
                    if (isFollowing) {
                        binding.followButton.text = "Unfollow"
                    } else {
                        binding.followButton.text = "Follow"
                    }
                }

                // Takip butonuna tıklama işlemi
                binding.followButton.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            // Toggle follow işleminin tamamlanmasını bekleyelim
                            userViewModel.toggleFollowStatus(currentUserId, targetUserId)

                            // Takipçi sayısını ve takip edilen sayısını güncelleyelim
                            userViewModel.loadFollowerCount(targetUserId)
                            userViewModel.loadFollowingCount(targetUserId)

                            // Takip işlemi başarılı olduktan sonra postları yükleyelim
                            // Burada `isFollowing`'i gözlemleyip doğru durumu elde edelim
                            userViewModel.isFollowing.observe(viewLifecycleOwner) { isFollowing ->
                                if (isFollowing != null && isFollowing) {
                                    // Eğer kullanıcı takip ediyorsa, postları yükle
                                    galleryViewModel.loadPosts()
                                    galleryViewModel.refreshPostsAfterFollow()
                                    galleryViewModel.loadPosts()
                                }
                            }
                        } catch (e: Exception) {
                            // Hata durumunu ele alabiliriz
                            Log.e("FollowError", "Takip işlemi sırasında bir hata oluştu", e)
                        }
                    }
                }


                binding.folowingTextView.setOnClickListener{
                    navigateToUserFollowing(targetUserId)
                }

                binding.folowersTextView.setOnClickListener{
                    navigateToUserFollowers(targetUserId)
                }



                // Örneğin: Glide ile resim yükleme işlemi yapılabilir
                // Glide.with(this).load(it.profileImageUrl).into(binding.profileImage)
            } ?: run {
                // Eğer kullanıcı bulunamazsa hata mesajı göster
                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
            }
        }

        // Kullanıcıyı userId ile yükleme işlemi
        galleryViewModel.getUserById(userId)
    }



    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment) // Use your actual container ID
            .addToBackStack(null) // Optional, if you want back navigation
            .commit()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryViewModel.loadPosts()


        val isOwner = arguments?.getBoolean(ARG_IS_FROM_SEARCH, false) ?: false
        binding.settingsIcon
            .setOnClickListener{
                replaceFragment(SettingsFragment())
            }


        if (isOwner){
            userId = arguments?.getString("userId") ?: ""
            if (userId.isNotEmpty()) {
                observePosts(userId)
            }
        }
        else {
            // Fragment argümanlarından userId'yi al

            userId=arguments?.getString("userId") ?: userPreferences.getUser()!!.id

            // ViewModel'den userId'yi çek
            ownerUser = x
            galleryViewModel.fetchUserId()

            // Postları gözlemle
            galleryViewModel.posts.observe(viewLifecycleOwner) { postsForUsers ->
                // Kullanıcı kimliğini al

                // Kullanıcı kimliğiyle postları filtrele
                val filteredPosts = postsForUsers.filter { post -> post.username == userPreferences.getUser()?.id }
                userPostAdapter.setPosts(filteredPosts)

            }
            galleryViewModel.currentUser.observe(viewLifecycleOwner) { user ->

                user?.let {
                    userPreferences.login(user)
                    // Kullanıcı bulundu, UI'yi güncelle
                    binding.usernameM.text = userPreferences.getUser()!!.id
                    binding.userHandle.text = "@${userPreferences.getUser()!!.name}"

                    // Kullanıcının profil resmini Glide ile yükle
                    Glide.with(this)
                        .load(it.profileImageUrl) // Kullanıcının profil resim URL'si
                        .placeholder(R.drawable.add) // Yükleme sırasında gösterilecek varsayılan resim
                        .error(R.drawable.sayfabitti) // Hata durumunda gösterilecek resim
                        .circleCrop() // Resmi yuvarlak yapar
                        .into(binding.profileImageP)
                    Glide.with(this)
                        .load(it.profileBacgroundImageUrl) // Kullanıcının profil resim URL'si
                        .placeholder(R.drawable.add) // Yükleme sırasında gösterilecek varsayılan resim
                        .error(R.drawable.sayfabitti) // Hata durumunda gösterilecek resim
                        .into(binding.mainBackgroundImage)

                    userViewModel.loadFollowingCount(currentUserId)
                    userViewModel.loadFollowerCount(currentUserId)
// Takip edilen kişi sayısını gözlemle
                    userViewModel.followingCount.observe(viewLifecycleOwner) { count ->
                        binding.folowingTextView.text = count.toString() // UI'da göstermek için
                    }


                    binding.folowingTextView.setOnClickListener{
                        navigateToUserFollowing(currentUserId)

                    }
                    userViewModel.followerCount.observe(viewLifecycleOwner) { count ->
                        binding.folowersTextView.text = count.toString() // UI'da göstermek için
                    }

                    binding.folowersTextView.setOnClickListener{
                        navigateToUserFollowers(currentUserId)
                    }


                } ?: run {
                    // Eğer kullanıcı bulunamazsa, default değerleri göster
                    binding.usernameM.text = galleryViewModel.IDGET
                    binding.userHandle.text = galleryViewModel.IDGET
                }

            }
            userPreferences.getUser()?.let { galleryViewModel.getUserById(it.id) }

            // Belirtilen userId ile kullanıcıyı getirmeye çalış


            // Kullanıcı bilgilerini gözlemle

        }



        // Initialize user post adapter
        userPostAdapter = UserPostAdapter(this)

        // Observe posts and filter by username

        //val isFromSearch = arguments?.getBoolean(ARG_IS_FROM_SEARCH) ?: false
        //isOwner = !isFromSearch // searchFragment'den gelmediyse true, geldiyse false

        // Observe posts and update UI
        lifecycleScope.launch {
            galleryViewModel.currentUserId.collect { currentUserId ->
                updateUIBasedOnOwnership()
            }
        }

        binding.recyclerViewPosts.apply {
            layoutManager = GridLayoutManager(context, 3) // 3-column grid
            adapter = userPostAdapter
        }
    }

    private fun updateUIBasedOnOwnership() {
        val isOwner=arguments?.getBoolean(ARG_IS_FROM_SEARCH, false) ?: false
        if (!isOwner) {
            binding.settingsIcon.visibility = View.VISIBLE
            binding.followButton.visibility = View.GONE
            binding.messageButton.visibility = View.GONE
        } else {
            binding.settingsIcon.visibility = View.GONE
            binding.followButton.visibility = View.VISIBLE
            binding.messageButton.visibility = View.VISIBLE
        }
    }

    override fun onItemClicked(post: Post) {
        val isOwner = arguments?.getBoolean(ARG_IS_FROM_SEARCH, false) ?: false
        val postDetailFragment: PostDetailFragment

        // You may not need to set the string variable if it's not used later
        val userId = if (isOwner) {
            // Assuming userId is defined in your UserProfileFragment
            userId
        } else {
           userId
        }
        val postId = post.id

        // Create the PostDetailFragment with the post
        postDetailFragment = PostDetailFragment.newInstance(userId,postId)

        // Start the fragment transaction
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, postDetailFragment)
            .addToBackStack(null)
            .commit()
    }


}
