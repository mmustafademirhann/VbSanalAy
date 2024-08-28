package com.example.socialmediavbsanalay.presentation

import android.animation.ObjectAnimator
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
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {


    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private var isMoved=false

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
        val animatedLayout=binding.animatedConstraintLayout
        val bacgImage=binding.fullscreenImage
        animatedLayout.setOnClickListener{
            animateLayout(animatedLayout)
        }
        bacgImage.setOnClickListener{
            closeAnimateLayout(animatedLayout)
        }
        binding.navigateButton.setOnClickListener {
            next(it)
        }


	//I am confused
    }
    private fun animateLayout(view: View) {
        val translationY = if (isMoved) 0f else -100f
        val animator = ObjectAnimator.ofFloat(view, "translationY", view.translationY, translationY)
        animator.duration = 300 // Duration of the animation in milliseconds
        animator.start()

        // Update the state
        isMoved = !isMoved
    }
    private fun closeAnimateLayout(view: View){
        val animator = ObjectAnimator.ofFloat(view, "translationY", 0f, 100f)
        animator.duration = 300 // Duration of the animation in milliseconds
        animator.start()

        isMoved = !isMoved
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    fun next(view: View){

        val action=WelcomeFragmentDirections.actionWelcomeFragmentToSignInFragment()
        Navigation.findNavController(view).navigate(action)
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
    //ı am tring to fix what i did
}
