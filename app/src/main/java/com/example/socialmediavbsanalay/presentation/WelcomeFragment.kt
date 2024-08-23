package com.example.socialmediavbsanalay.presentation

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.socialmediavbsanalay.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private val hideHandler = Handler(Looper.myLooper()!!)
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private var isExpanded = false

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

        val fullscreenImage = binding.fullscreenImage
        val hiddenImage = binding.welcomeBarContainer

        fullscreenImage.setOnClickListener {
            toggleImages(hiddenImage, fullscreenImage)
        }

        hideSystemUI()
	//I am confused
    }

    private fun toggleImages(hiddenImage: View, fullscreenImage: View) {
        val targetHeight = (resources.displayMetrics.heightPixels * 0.45).toInt()
        val initialHeight = (resources.displayMetrics.heightPixels * 0.20).toInt()

        val layoutParams = hiddenImage.layoutParams

        if (isExpanded) {
            // Collapse images
            ValueAnimator.ofInt(layoutParams.height, initialHeight).apply {
                duration = 500
                addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    hiddenImage.layoutParams.height = animatedValue
                    hiddenImage.requestLayout()
                }
                start()
            }
            // Move fullscreen image down by the difference in height
            ValueAnimator.ofInt(0, (targetHeight - initialHeight)).apply {
                duration = 500
                addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    fullscreenImage.translationY = animatedValue.toFloat()
                }
                start()
            }
        } else {
            // Expand images
            ValueAnimator.ofInt(layoutParams.height, targetHeight).apply {
                duration = 500
                addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    hiddenImage.layoutParams.height = animatedValue
                    hiddenImage.requestLayout()
                }
                start()
            }
            // Move fullscreen image up by the difference in height
            ValueAnimator.ofInt(0, (targetHeight - initialHeight)).apply {
                duration = 500
                addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    fullscreenImage.translationY = -animatedValue.toFloat()
                }
                start()
            }
        }
        isExpanded = !isExpanded
    }

    private fun hideSystemUI() {
        val flags =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        activity?.window?.decorView?.systemUiVisibility = flags
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
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
