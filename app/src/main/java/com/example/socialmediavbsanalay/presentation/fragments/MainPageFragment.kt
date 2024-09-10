package com.example.socialmediavbsanalay.presentation.fragments

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentMainPageBinding
import com.example.socialmediavbsanalay.presentation.adapters.PostAdapter
import com.example.socialmediavbsanalay.presentation.adapters.StoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavOptions
import com.example.socialmediavbsanalay.presentation.MainActivity


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

    private fun navigateToSearchResultsFragment(view: View) {
        val action =MainPageFragmentDirections.actionMainPageFragmentToSearchUserPostFragment()
        Navigation.findNavController(view).navigate(action)
    }


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

    private fun accessGallery() {
        // Galeriden fotoğraf çekme işlemleri burada yapılacak
        // MediaStore veya Intent kullanarak galeriye erişebilirsiniz
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 123
        //private val REQUEST_IMAGE_CAPTURE = 1
        //private val PICK_IMAGE = 2
        //private val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showBottomBar()
        adapterFunctions()
        binding.editTextText3.setOnClickListener {
            navigateToSearchResultsFragment(it)
        }
        // Load your stories into the adapter



    }
    private fun navigateToSame(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
    private fun adapterFunctions() {
        storyAdapter = StoryAdapter()

        binding.storyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = storyAdapter
        }
        postAdapter = PostAdapter()

        binding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = postAdapter
        }
        val stories =
            storyAdapter.loadStories() // Implement this function to get your list of stories
        storyAdapter.setStories(stories)

        val posts = postAdapter.loadPosts() // Implement this function to get your list of stories
        postAdapter.setPosts(posts)
    }


    // ContextCompat.checkSelfPermission(
    // requireContext(),
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}