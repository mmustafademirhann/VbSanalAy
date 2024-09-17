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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.ActivityMainBinding

import com.example.socialmediavbsanalay.presentation.fragments.MainPageFragment

import com.example.socialmediavbsanalay.presentation.fragments.MessageFragment
import com.example.socialmediavbsanalay.presentation.fragments.NotificationBarFragment
import com.example.socialmediavbsanalay.presentation.fragments.SignInFragment
import com.example.socialmediavbsanalay.presentation.fragments.SignUpFragment
import com.example.socialmediavbsanalay.presentation.fragments.UserProfileFragment
import com.example.socialmediavbsanalay.presentation.fragments.WelcomeFragment
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController
    val database = Firebase.database
    val myRef = database.getReference("message")
    private val galleryViewModel: GalleryViewModel by viewModels()
    private val fragmentCache = mutableMapOf<String, Fragment>()



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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val sharedPreferences: SharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isSignedIn = sharedPreferences.getBoolean("is_signed_in", false)

        if (savedInstanceState == null) {
            if (isSignedIn) {
                // If user is signed in, navigate to MainPageFragment
                switchFragment(MainPageFragment::class.java)
            } else {
                // If user is not signed in, navigate to WelcomeFragment
                switchFragment(WelcomeFragment::class.java)
            }
        }
        galleryViewModel.uploadStatus.observe(this) { status ->

            // Update UI with upload status or refresh the fragment view
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
        clickEvents()

        // Rest of the onCreate code...
    }



    private fun clickEvents(){
        binding.userImageViewLayout.setOnClickListener {
            setVisibilityForLine(binding.userline)
            switchFragment(UserProfileFragment::class.java)
        }
        binding.homeImageViewLayout.setOnClickListener {
            setVisibilityForLine(binding.homeline)
            switchFragment(MainPageFragment::class.java)
        }
        binding.messagetwoiconLayout.setOnClickListener {
            setVisibilityForLine(binding.messageline)
            switchFragment(MessageFragment::class.java)
        }
        binding.notificationImageViewLayout.setOnClickListener {
            setVisibilityForLine(binding.notificationline)
            switchFragment(NotificationBarFragment::class.java)
        }
        binding.addButton.setOnClickListener{

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
    fun handleImageUri(uri: Uri) {
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
    fun switchFragment(fragmentClass: Class<out Fragment>) {
        val fragment = getFragment(fragmentClass)
        val transaction = supportFragmentManager.beginTransaction()

        // Optional: Add animations for smoother transitions (customize as needed)

        transaction.replace(R.id.fragmentContainerView, fragment)

        // Add to back stack unless it's WelcomeFragment or MainPageFragment
        if (fragment !is WelcomeFragment && fragment !is MainPageFragment) {
            transaction.addToBackStack(null)
        }

        transaction.commit()

        // Manage bottom bar visibility
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
        setVisibilityForLine(binding.homeline)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        /*supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, MainPageFragment(), MainPageFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()*/
        when (currentFragment) {
            is MessageFragment, is NotificationBarFragment, is UserProfileFragment -> {
                // If currently on one of these fragments, go back to MainPageFragment
                switchFragment(MainPageFragment::class.java)
            }
            is MainPageFragment -> {
                // If already on MainPageFragment, exit the app
                finishAffinity()
            }
            is WelcomeFragment, is SignInFragment, is SignUpFragment -> {
                // Let NavController handle back navigation for the sign-in flow
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
