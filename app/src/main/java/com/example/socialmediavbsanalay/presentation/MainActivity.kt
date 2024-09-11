package com.example.socialmediavbsanalay.presentation

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
    val database = Firebase.database
    val myRef = database.getReference("message")
    private val postViewModel: GalleryViewModel by viewModels()



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

        setContentView(R.layout.activity_main)
        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                accessGallery()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PICK_IMAGE_REQUEST)
            }
        }

        postViewModel.uploadStatus.observe(this) { status ->
            // You might want to notify the fragment about the upload status
            // For example, use a LiveData or any other communication method
        }
        // Ensure that the navigation component is working with the correct NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        myRef.setValue("Hello, World!")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        var xyz=true
        setContentView(view)
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        clickEvents()

    }

    private fun clickEvents(){
        binding.userImageViewLayout.setOnClickListener {
            setVisibilityForLine(binding.userline)
            switchFragment(UserProfileFragment())
        }
        binding.homeImageViewLayout.setOnClickListener {
            setVisibilityForLine(binding.homeline)
            switchFragment(MainPageFragment())
        }
        binding.messagetwoiconLayout.setOnClickListener {
            setVisibilityForLine(binding.messageline)
            switchFragment(MessageFragment())
        }
        binding.notificationImageViewLayout.setOnClickListener {
            setVisibilityForLine(binding.notificationline)
            switchFragment(NotificationBarFragment())
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
                (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? MainPageFragment)
                    ?.handleImageUri(uri)
            }
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
    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()

        if (fragment is WelcomeFragment) {
            hideBottomBar()
        } else {
            showBottomBar()
        }
    }
     fun showBottomBar() {
        binding.bottomMain.visibility = View.VISIBLE
    }

     fun hideBottomBar() {
        binding.bottomMain.visibility = View.GONE
    }


    override fun onBackPressed() {

        setVisibilityForLine(binding.homeline)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        /*supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, MainPageFragment(), MainPageFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()*/
        when (currentFragment) {
            is MessageFragment, is NotificationBarFragment, is UserProfileFragment -> {
                // If currently on one of these fragments, go back to MainPageFragment
                switchFragment(MainPageFragment())
            }
            is MainPageFragment -> {
                // If already on MainPageFragment, exit the app
                finish()
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
