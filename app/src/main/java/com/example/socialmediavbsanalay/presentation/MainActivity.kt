package com.example.socialmediavbsanalay.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.ActivityMainBinding
import com.example.socialmediavbsanalay.presentation.fragments.HomeFragment
import com.example.socialmediavbsanalay.presentation.fragments.MainPageFragment
import com.example.socialmediavbsanalay.presentation.fragments.MessageFragment
import com.example.socialmediavbsanalay.presentation.fragments.NotificationBarFragment
import com.example.socialmediavbsanalay.presentation.fragments.UserProfileFragment
import com.example.socialmediavbsanalay.presentation.fragments.WelcomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        var xyz=true
        setContentView(view)
        if (savedInstanceState == null) {

        }
        clickEvents()

    }
    // In your MainActivity

    private fun clickEvents(){
        binding.userImageView.setOnClickListener {
            switchFragment(UserProfileFragment())
        }
        binding.homeImageView.setOnClickListener {
            switchFragment(MainPageFragment())
        }
        binding.messagetwoicon.setOnClickListener {
            switchFragment(MessageFragment())
        }
        binding.notificationImageView.setOnClickListener {
            switchFragment(NotificationBarFragment())
        }

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
        /**/

        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) is MessageFragment ||
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) is NotificationBarFragment ||
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) is UserProfileFragment) {
            switchFragment(MainPageFragment())
        } else {
            super.onBackPressed()
        }

    }
}
