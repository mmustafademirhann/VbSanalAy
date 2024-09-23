package com.example.socialmediavbsanalay.presentation.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.socialmediavbsanalay.R
import com.example.socialmediavbsanalay.databinding.FragmentSettingsBinding
import com.example.socialmediavbsanalay.presentation.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // FirebaseAuth başlat
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding başlat
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Logout butonuna tıklanıldığında yapılacak işlemler
        binding.logOutButton.setOnClickListener {
            signOutAndNavigateToWelcome()
        }
    }

    private fun signOutAndNavigateToWelcome() {
        signOut() // Firebase oturum kapatma
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear() // Tüm SharedPreferences değerlerini temizle
            apply()
        }

        // Uygulamayı baştan başlatmak
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // Aktiviteleri sıfırla
        startActivity(intent)
        Toast.makeText(requireContext(), "Çıkış yapıldı.", Toast.LENGTH_SHORT).show()
    }

    private fun signOut() {
        auth.signOut() // Firebase kullanıcıdan çıkış yapar
    }

    private fun navigateToWelcomeFragment() {
        // WelcomeFragment'e geçiş
        val welcomeFragment = WelcomeFragment()
        val fragmentManager: FragmentManager? = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.fragmentContainerView, welcomeFragment) // Fragment'i değiştir
        fragmentTransaction?.addToBackStack(null) // Geri tuşuyla geri dönebilmek için
        fragmentTransaction?.commit() // İşlemi gerçekleştir
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Memory leak'i önlemek için
    }
}
