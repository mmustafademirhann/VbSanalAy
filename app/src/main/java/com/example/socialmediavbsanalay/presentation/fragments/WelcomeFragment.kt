package com.example.socialmediavbsanalay.presentation.fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentWelcomeBinding
import com.example.socialmediavbsanalay.presentation.MainActivity

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private var isMoved = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomBar()
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isSignedIn = sharedPreferences.getBoolean("is_signed_in", false)

        if (!isSignedIn) {
            // Kullanıcı çıkış yaptı, UI'yi güncelle
            //binding.welcomeMessage.text = "Hoş geldiniz! Giriş yapın."
            binding.navigateButton.visibility = View.VISIBLE
        } else {
            // Kullanıcı giriş yaptı, farklı bir durum
            //binding.welcomeMessage.text = "Hoş geldiniz geri!"
            binding.navigateButton.visibility = View.GONE
        }
        updateUI(isSignedIn)

        initClickListeners()
    }
    private fun updateUI(isSignedIn: Boolean) {
        if (!isSignedIn) {
            binding.navigateButton.visibility = View.VISIBLE
        } else {
            binding.navigateButton.visibility = View.GONE
        }
    }

    private fun initClickListeners() {
        val animatedLayout = binding.animatedConstraintLayout
        val bacgImage = binding.fullscreenImage
        animatedLayout.setOnClickListener {
            animateLayout(animatedLayout)
        }
        bacgImage.setOnClickListener {
            closeAnimateLayout(animatedLayout)
        }
        binding.navigateButton.setOnClickListener {
            // Use FragmentManager to replace the fragment
            val signInFragment = SignInFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, signInFragment) // Adjust the container ID if needed
                .addToBackStack(null) // Optionally add to back stack
                .commit()
        }
    }

    private fun animateLayout(view: View) {
        val translationY = if (isMoved) 0f else -100f
        val animator = ObjectAnimator.ofFloat(view, "translationY", view.translationY, translationY)
        animator.duration = 300 // Duration of the animation in milliseconds
        animator.start()

        // Update the state
        isMoved = !isMoved
    }

    private fun closeAnimateLayout(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "translationY", 0f, 100f)
        animator.duration = 300 // Duration of the animation in milliseconds
        animator.start()

        isMoved = !isMoved
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.decorView?.systemUiVisibility = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
