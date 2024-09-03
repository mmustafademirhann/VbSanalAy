package com.example.socialmediavbsanalay.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.ActivityMainBinding

import com.example.socialmediavbsanalay.presentation.fragments.MainPageFragment

import com.example.socialmediavbsanalay.presentation.fragments.MessageFragment
import com.example.socialmediavbsanalay.presentation.fragments.NotificationBarFragment
import com.example.socialmediavbsanalay.presentation.fragments.UserProfileFragment
import com.example.socialmediavbsanalay.presentation.fragments.WelcomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            accessGallery()
        } else {
            // İzin reddedildiğinde kullanıcıya bilgilendirme yap
            Toast.makeText(requireContext(), "İzin gerekli!", Toast.LENGTH_SHORT).show()
        }
    }



    companion object{
        private const val GALLERY_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        var xyz=true
        setContentView(view)

        clickEvents()

    }

    private fun clickEvents(){
        binding.userImageView.setOnClickListener {
            setVisibilityForLine(binding.userline)
            switchFragment(UserProfileFragment())
        }
        binding.homeImageView.setOnClickListener {
            setVisibilityForLine(binding.homeline)
            switchFragment(MainPageFragment())
        }
        binding.messagetwoicon.setOnClickListener {
            setVisibilityForLine(binding.messageline)
            switchFragment(MessageFragment())
        }
        binding.notificationImageView.setOnClickListener {
            setVisibilityForLine(binding.notificationline)
            switchFragment(NotificationBarFragment())
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
                    requireContext(),
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
                    requireContext(),
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
        AlertDialog.Builder(requireContext())
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
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) is MessageFragment ||
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) is NotificationBarFragment ||
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) is UserProfileFragment) {
            switchFragment(MainPageFragment())
        } else {
            super.onBackPressed()
        }

    }


    private fun accessGallery() {
        // Galeriden fotoğraf çekme işlemleri burada yapılacak
        // MediaStore veya Intent kullanarak galeriye erişebilirsiniz
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }
}
