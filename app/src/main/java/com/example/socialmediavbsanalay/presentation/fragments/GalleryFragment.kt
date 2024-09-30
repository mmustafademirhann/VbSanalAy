package com.example.socialmediavbsanalay.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socialmediavbsanalay.databinding.FragmentGalleryBinding
import com.example.socialmediavbsanalay.presentation.adapters.GalleryAdapter
import com.example.socialmediavbsanalay.presentation.viewModels.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    @Inject lateinit var galleryAdapter: GalleryAdapter
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var galleryViewModel: GalleryViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      /*  galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        galleryAdapter = GalleryAdapter()

        binding.galleryRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)  // 3 columns in grid
            adapter = galleryAdapter
        }

        galleryViewModel.recentPhotos.observe(viewLifecycleOwner) { images ->
            galleryAdapter.submitList(images)
        }*/
        //loadGalleryImages()

    }
    private fun loadGalleryImages() {
        // This will trigger the loading of images from the gallery
        galleryViewModel.loadGalleryImages()
    }







    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
