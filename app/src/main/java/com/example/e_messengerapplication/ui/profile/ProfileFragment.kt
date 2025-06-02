package com.example.e_messengerapplication.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.databinding.FragmentProfileBinding
import com.example.e_messengerapplication.ui.MainActivity
import com.example.e_messengerapplication.ui.auth.AuthViewModel
import com.example.e_messengerapplication.utils.Constant.TAG_USER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: SharedUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setSelectedBottomMenuItem(R.id.profileFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpData()
        listenEvent()
    }

    private fun initView() {
        // Nếu bạn cần khởi tạo view mặc định hay style theme
    }

    private fun setUpData() {
        userViewModel.loadUser()
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.user.collectLatest { user ->
                if (user != null) {
                    Log.d(TAG_USER, "User: ${user.avatarUrl}")
                    binding.tvDisplayName.text = user.displayName
                    binding.tvBio.text = user.bio
                    var url = user.avatarUrl
                    if (url == "") url =
                        "http://res.cloudinary.com/dtvuu5b2g/image/upload/v1746720818/other_default_avatar_fif3lm.jpg"
                    val secureUrl = url.replace("http://", "https://")
                    Glide.with(requireContext())
                        .load(secureUrl)
                        .into(binding.imageAvatar)
                }
            }
        }
    }

    private fun listenEvent() {
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
            findNavController().navigate(R.id.signInFragment)
        }

        // Optional: Dark mode toggle
        /*
        val sharedPrefs = requireContext().getSharedPreferences("settingsPrefs", Context.MODE_PRIVATE)
        binding.switchTheme.isChecked = sharedPrefs.getBoolean("dark_mode", false)

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
            sharedPrefs.edit().putBoolean("dark_mode", isChecked).apply()

            // Optional: recreate current fragment to apply theme
            findNavController().navigate(findNavController().currentDestination!!.id)
        }
        */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
