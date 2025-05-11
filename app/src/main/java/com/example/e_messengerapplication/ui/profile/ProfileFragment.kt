package com.example.e_messengerapplication.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.databinding.FragmentProfileBinding
import com.example.e_messengerapplication.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

//        val sharedPrefs = requireContext().getSharedPreferences("settingsPrefs", Context.MODE_PRIVATE)
//        when (sharedPrefs.getBoolean("dark_mode", false)) {
//            true -> {
//                binding.switchTheme.isChecked = true
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            }
//            else -> {
//                binding.switchTheme.isChecked = false
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//        }
        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
            findNavController().clearBackStack(R.id.signInFragment)
        }

//        binding.switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
//            val currentDest = findNavController().currentDestination?.id
//            val mode = if (isChecked) {
//                AppCompatDelegate.MODE_NIGHT_YES
//            } else {
//                AppCompatDelegate.MODE_NIGHT_NO
//            }
//            AppCompatDelegate.setDefaultNightMode(mode)
//            findNavController().navigate(currentDest!!)

//            sharedPrefs.edit().putBoolean("dark_mode", isChecked).apply()
//        }

    }
}