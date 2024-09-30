package com.example.socialmediavbsanalay.presentation.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.data.dataSource.UserPreferences
import com.example.socialmediavbsanalay.databinding.FragmentSettingsBinding
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var userPreferences: UserPreferences

    private val galleryViewModel: GalleryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private var isSelectingBackground: Boolean = false // Profil mi arka plan mı seçiliyor?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Galeriden resim seçmek için izin gerekli.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    imageUri = uri
                    if (isSelectingBackground) {
                        binding.backgroundImage.setImageURI(imageUri)
                        imageUri?.let { galleryViewModel.uploadBacgroundPicturee(it) }
                    } else {
                        binding.profileImage.setImageURI(imageUri)
                        imageUri?.let { galleryViewModel.uploadProfilePicturee(it) }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logOutButton.setOnClickListener {
            userPreferences.deleteUser()
            signOutAndNavigateToWelcome()
        }

        binding.profileEditIcon.setOnClickListener {
            isSelectingBackground = false
            handleProfileEditIconClick()
        }

        binding.backgroundEditIcon.setOnClickListener {
            isSelectingBackground = true
            handleProfileEditIconClick()
        }

        val email = FirebaseAuth.getInstance().currentUser?.email
        email?.let {
            galleryViewModel.getUserProfileImage(it)
            galleryViewModel.getBacgroudImage(it)
        }

        galleryViewModel.profileImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            if (imageUrl != null) {
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.add)
                    .error(R.drawable.add)
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.add)
            }
        }

        galleryViewModel.profilebImageUrl.observe(viewLifecycleOwner) { backgroundUrl ->
            if (backgroundUrl != null) {
                Glide.with(this)
                    .load(backgroundUrl)
                    .placeholder(R.drawable.add)
                    .error(R.drawable.sayfabitti
                    )
                    .into(binding.backgroundImage)
            } else {
                binding.backgroundImage.setImageResource(R.drawable.sayfabitti)
            }
        }
    }

    private fun handleProfileEditIconClick() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                checkAndRequestPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
            else -> {
                checkAndRequestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun checkAndRequestPermission(permission: String) {
        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(requireContext(), "Profil resmi seçmek için izin vermeniz gerekiyor.", Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(permission)
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun signOutAndNavigateToWelcome() {
        signOut()
        clearSharedPreferences()

        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
        Toast.makeText(requireContext(), "Çıkış yapıldı.", Toast.LENGTH_SHORT).show()
    }

    private fun signOut() {
        auth.signOut()
    }

    private fun clearSharedPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
