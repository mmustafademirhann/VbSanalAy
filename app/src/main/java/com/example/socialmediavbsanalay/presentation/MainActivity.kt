package com.example.socialmediavbsanalay.presentation

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.ActivityMainBinding
import com.example.socialmediavbsanalay.presentation.fragments.MainPageFragment
import com.example.socialmediavbsanalay.presentation.fragments.MessageFragment
import com.example.socialmediavbsanalay.presentation.fragments.NotificationBarFragment
import com.example.socialmediavbsanalay.presentation.fragments.SignInFragment
import com.example.socialmediavbsanalay.presentation.fragments.SignUpFragment
import com.example.socialmediavbsanalay.presentation.fragments.UserFollowersFragment
import com.example.socialmediavbsanalay.presentation.fragments.UserProfileFragment
import com.example.socialmediavbsanalay.presentation.fragments.WelcomeFragment
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.database
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController
    val database = Firebase.database
    val myRef = database.getReference("message")
    private val galleryViewModel: GalleryViewModel by viewModels()
    private val fragmentCache = mutableMapOf<String, Fragment>()
    //sldnlaksdn


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            accessGallery()
        } else {
            // İzin reddedildiğinde kullanıcıya bilgilendirme yap
            Toast.makeText(this, "İzin gerekli!", Toast.LENGTH_SHORT).show()
        }
    }



    companion object{
        private const val GALLERY_REQUEST_CODE = 123
        private const val PICK_IMAGE_REQUEST = 1
    }


    // Function to apply the scaling and alpha animation
    private fun applyAnimation(view: View) {
        view.animate()
            .scaleX(0.9f) // Scale down slightly
            .scaleY(0.9f)
            .alpha(0.5f) // Change opacity to give a pressed effect
            .setDuration(100) // Duration of the press effect
            .withEndAction {
                // Revert back to the original size and opacity
                view.animate()
                    .scaleX(1.0f) // Reset scale
                    .scaleY(1.0f)
                    .alpha(1.0f) // Reset opacity
                    .setDuration(100) // Duration of the reset effect
                    .start()
            }
            .start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        if (navHostFragment == null) {
            Log.e(TAG, "NavHostFragment is null!")
        } else {
            navController = navHostFragment.navController
        }
        //navController = navHostFragment.navController

        val sharedPreferences: SharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isSignedIn = sharedPreferences.getBoolean("is_signed_in", false)

        if (savedInstanceState == null) {
            if (isSignedIn) {
                // If user is signed in, navigate to MainPageFragment
                switchFragment(MainPageFragment::class.java,true)
            } else {
                // If user is not signed in, navigate to WelcomeFragment
                switchFragment(WelcomeFragment::class.java,true)
            }
        }
        galleryViewModel.uploadStatus.observe(this) { status ->

            // Update UI with upload status or refresh the fragment view
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
        clickEvents()

        // Rest of the onCreate code...
    }



    fun clickEvents() {
        // List of all layouts with corresponding fragments and lines to be updated
        val layoutActions = listOf(
            Triple(binding.homeImageViewLayout, MainPageFragment::class.java, binding.homeline),
            Triple(binding.messagetwoiconLayout, MessageFragment::class.java, binding.messageline),
            Triple(binding.userImageViewLayout, UserProfileFragment::class.java, binding.userline),
            Triple(binding.notificationImageViewLayout, NotificationBarFragment::class.java, binding.notificationline)
        )

        // Set up click listeners with animation for each layout
        layoutActions.forEach { (layout, fragmentClass, line) ->
            layout.setOnClickListener { view ->
                applyAnimation(view) // Apply animation on click
                setVisibilityForLine(line) // Perform specific action
                switchFragment(fragmentClass, true) // Switch fragment as intended
            }

            // Apply click animation for each child inside the layout
            for (i in 0 until layout.childCount) {
                val child = layout.getChildAt(i)
                child.setOnClickListener {
                    applyAnimation(child) // Apply animation to the child view
                    layout.performClick() // Trigger the layout's click event for fragment switch
                }
            }
        }

        // Handle click event for the add button separately
        binding.addButton.setOnClickListener {
            applyAnimation(it) // Optionally animate the button
            checkPermission()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Notify the fragment with the selected image URI
                handleImageUri(uri)

            }
        }

    }
    private fun handleImageUri(uri: Uri) {
        val userId = galleryViewModel.getUserId() // Get userId from ViewModel/Interactor
        if (userId != null) {
            galleryViewModel.uploadPhoto(uri) // Pass image URI and userId to upload function
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setVisibilityForLine(visibleLine: View) {
        binding.userline.visibility = View.INVISIBLE
        binding.homeline.visibility = View.INVISIBLE
        binding.messageline.visibility = View.INVISIBLE
        binding.notificationline.visibility = View.INVISIBLE

        visibleLine.visibility = View.VISIBLE
    }
    private fun setVisibilityForLineFollow(visibleLine: View) {
        binding.userline.visibility = View.VISIBLE
        binding.homeline.visibility = View.INVISIBLE
        binding.messageline.visibility = View.INVISIBLE
        binding.notificationline.visibility = View.INVISIBLE

        visibleLine.visibility = View.INVISIBLE
    }
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 ve üstü için izin kontrolü
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // İzin verilmişse galeriye eriş
                    accessGallery()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                    // Kullanıcı daha önce izni reddettiyse, neden gerekli olduğunu açıkla
                    explainPermission()
                }

                else -> {
                    // İzin isteme
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
        } else {
            // Android 12 ve öncesi için izin kontrolü
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // İzin verilmişse galeriye eriş
                    accessGallery()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // Kullanıcı daha önce izni reddettiyse, neden gerekli olduğunu açıkla
                    explainPermission()
                }

                else -> {
                    // İzin isteme
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }


    private fun explainPermission() {
        // Kullanıcıya iznin neden gerektiğini açıklayan bir dialog göster
        AlertDialog.Builder(this)
            .setTitle("İzin Gerekli")
            .setMessage("Bu uygulamanın fotoğraflara erişebilmesi için izne ihtiyacı var.")
            .setPositiveButton("Tamam") { _, _ ->
                // İzni tekrar iste
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton("İptal", null)
            .show()
    }
    private fun switchFragment(fragmentClass: Class<out Fragment>, isOwner: Boolean) {
        val fragment = getFragment(fragmentClass).apply {
            arguments = Bundle().apply {
                putBoolean("isOwner", isOwner) // isOwner'ı argüman olarak ekleyin
            }
        }

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.fragmentContainerView, fragment)

        // Back stack işlemi
        if (fragment !is WelcomeFragment && fragment !is MainPageFragment) {
            transaction.addToBackStack(null)
        }

        transaction.commit()

        // Bottom bar görünürlüğü yönetimi
        if (fragment is WelcomeFragment) {
            hideBottomBar()
        } else {
            showBottomBar()
        }
    }


    private fun getFragment(fragmentClass: Class<out Fragment>): Fragment {
        val fragmentTag = fragmentClass.simpleName
        return fragmentCache.getOrPut(fragmentTag) {
            fragmentClass.newInstance()
        }
    }
    private fun getFragmentt(fragmentClass: Class<out Fragment>): Fragment {
        return when (fragmentClass) {
            UserProfileFragment::class.java -> UserProfileFragment()
            else -> throw IllegalArgumentException("Unknown fragment class: $fragmentClass")
        }
    }








    // Method to navigate to the first instance of a fragment


    fun showBottomBar() {
        binding.bottomMain.visibility = View.VISIBLE
    }

     fun hideBottomBar() {
        binding.bottomMain.visibility = View.GONE
    }

    private fun goToFirstInstanceOfFragment(fragmentClass: Class<out Fragment>) {
        val fragmentManager = supportFragmentManager

        // Back stack'te olan fragmentleri temizle
        while (fragmentManager.backStackEntryCount > 0) {
            val backStackEntry = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1)
            if (backStackEntry.name == fragmentClass.simpleName) {
                // İstenilen fragment bulunduğunda bu fragment'e geri dön
                fragmentManager.popBackStack(backStackEntry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                return
            } else {
                fragmentManager.popBackStack()
            }
        }
    }

    override fun onBackPressed() {
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        //val navController = navHostFragment.navController


        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        /*supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, MainPageFragment(), MainPageFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()*/
        when (currentFragment) {
            is UserProfileFragment -> {
                if (supportFragmentManager.backStackEntryCount > 0) {

                    setVisibilityForLine(binding.homeline)
                    super.onBackPressed()

                }
                // Eğer yığın boş ise, uygulamanın ana sayfasına git
                else {
                    setVisibilityForLine(binding.homeline)
                    switchFragment(MainPageFragment::class.java, true)
                }

            }
            is MessageFragment, is NotificationBarFragment -> {

                setVisibilityForLine(binding.homeline)
                // If currently on one of these fragments, go back to MainPageFragment
                switchFragment(MainPageFragment::class.java,true)
            }
            is MainPageFragment, is WelcomeFragment -> {

                setVisibilityForLine(binding.homeline)
                // If already on MainPageFragment, exit the app
                finishAffinity()
            }
            is SignInFragment, is SignUpFragment -> {
                // Let NavController handle back navigation for the sign-in flow
                setVisibilityForLine(binding.homeline)
                super.onBackPressed()
            }
            is UserFollowersFragment -> {
                // Let NavController handle back navigation for the sign-in flow
                setVisibilityForLineFollow(binding.homeline)
                super.onBackPressed()
            }

            else -> {
                // Default behavior for other fragments, or if no specific action is required

                super.onBackPressed()
            }
        }
    }



    private fun accessGallery() {
        // Galeriden fotoğraf çekme işlemleri burada yapılacak
        // MediaStore veya Intent kullanarak galeriye erişebilirsiniz
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }


}
