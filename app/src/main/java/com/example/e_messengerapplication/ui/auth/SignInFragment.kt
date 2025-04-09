package com.example.e_messengerapplication.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.databinding.FragmentSignInBinding
import com.example.e_messengerapplication.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnListener()
    }

    private fun setOnListener() {
        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.btnSignin.setOnClickListener {
            val phoneNumber = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            viewModel.login(phoneNumber, password)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.isLoginSuccess.collect { isLoginSuccess ->
                    if (isLoginSuccess == true) {
                        Log.d("AUTH", "Login success")
                    } else if (isLoginSuccess == false) {
                        // Handle login failure
                    }
                }
            }
        }
    }

        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }
    }