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
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {


    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!



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

        binding.navigateButton.setOnClickListener {
            next(it)
        }

	//I am confused
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
