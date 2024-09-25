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
import androidx.lifecycle.lifecycleScope
import com.example.socialmediavbsanalay.databinding.FragmentSettingsBinding
import com.example.socialmediavbsanalay.domain.interactor.user.UserInteractor
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import com.example.socialmediavbsanalay.presentation.viewModels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.migration.CustomInjection.inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // İzin isteme işlemi için launcher tanımlaması
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Eğer izin verildiyse galeriyi aç
                openGallery()
            } else {
                // İzin verilmediyse kullanıcıya bildirimde bulun
                Toast.makeText(
                    requireContext(),
                    "Galeriden resim seçmek için izin gerekli.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Galeri açma işlemi için launcher tanımlaması
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    imageUri = uri
                    binding.profileImage.setImageURI(imageUri) // Seçilen resmi ImageView'a yerleştir
                    imageUri?.let { galleryViewModel.uploadProfilePicturee(it) }

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
            signOutAndNavigateToWelcome()
        }

        binding.profileEditIcon.setOnClickListener {
            handleProfileEditIconClick()
        }

        // Upload durumu izleme
        galleryViewModel.uploadStatus.observe(viewLifecycleOwner) { status ->
            Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
        }
        galleryViewModel.uploadStatus.observe(viewLifecycleOwner) { status ->
            if (status == "Yükleme başarılı") {
                imageUri?.let { uri ->
                    viewLifecycleOwner.lifecycleScope.launch { // Coroutine başlat
                        try {
                            val imageUrl = galleryViewModel.uploadProfilePicturee(uri)// Doğru şekilde çağır
                            val email = FirebaseAuth.getInstance().currentUser?.email
                            if (email != null) {
                                galleryViewModel.updateUserProfileImage(email, imageUrl.toString())
                            }
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Resim yükleme hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    // Profil düzenleme simgesine tıklanınca çağrılan metot
    private fun handleProfileEditIconClick() {
        // İzin kontrolü yapılıyor
        when {
            // Android 13 ve sonrası için farklı izin gereksinimleri olabilir.
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13 (API 33) için farklı izinler kontrol edilebilir
                checkAndRequestPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
            else -> {
                // Android 13'ten düşük sürümler için klasik izin kontrolü
                checkAndRequestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun checkAndRequestPermission(permission: String) {
        when {
            // Eğer izin verilmişse direkt galeriyi aç
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            // İzin verilmemişse ve kullanıcıya sebebini gösterme gerekliliği varsa
            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(requireContext(), "Profil resmi seçmek için izin vermeniz gerekiyor.", Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(permission)
            }
            // İzin daha önce sorulmamışsa veya kullanıcı reddettiyse tekrar izin iste
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    // Galeriyi açmak için kullanılan metot
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
