package com.example.e_messengerapplication.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_messengerapplication.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private var pickImageLauncher: ActivityResultLauncher<Intent>? = null
    private var selectedImageUri: Uri? = null

    private val userViewModel: SharedUserViewModel by activityViewModels()
    private val editProfileViewModel: EditProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLauncher()
        setUpData()
        listenEvent()
    }

    private fun listenEvent() {
        binding.btnChangeAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageLauncher?.launch(intent)
        }
        binding.btnSave.setOnClickListener {
            val displayName = binding.edtDisplayName.text.toString()
            val email = binding.edtEmail.text.toString()
            val bio = binding.edtBio.text.toString()
            val phoneNumber = binding.edtPhonenumber.text.toString()
            editProfileViewModel.updateProfile(displayName, email, bio, phoneNumber)
            selectedImageUri?.let { it1 ->
                editProfileViewModel.updateAvatar(requireContext(),
                    it1
                )
            }
            observeUpdate()
        }
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun observeUpdate() {
        viewLifecycleOwner.lifecycleScope.launch {
            editProfileViewModel.updateSuccess.collect {
                if (it) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Update profile success", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                else {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpData() {
        val currentUser = userViewModel.user.value
        if (currentUser != null) {
            binding.edtDisplayName.setText(currentUser.displayName)
            binding.edtEmail.setText(currentUser.email)
            binding.edtBio.setText(currentUser.bio)
            binding.edtPhonenumber.setText(currentUser.phoneNumber)
            var url = currentUser.avatarUrl
            if (url == "") url =
                "http://res.cloudinary.com/dtvuu5b2g/image/upload/v1746720818/other_default_avatar_fif3lm.jpg"
            val secureUrl = url.replace("http://", "https://")
            Glide.with(requireContext())
                .load(secureUrl)
                .into(binding.imageAvatar)
        }
    }

    private fun initLauncher() {
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    Log.d("PROFILE", "Image URI: $it")
                    Glide.with(requireContext())
                        .load(it)
                        .into(binding.imageAvatar)
                    selectedImageUri = it
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}