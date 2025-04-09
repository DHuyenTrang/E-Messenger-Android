package com.example.e_messengerapplication.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.databinding.FragmentSearchContactBinding
import com.example.e_messengerapplication.viewmodel.SearchContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchContactFragment : Fragment() {
    private var _binding: FragmentSearchContactBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val phoneNumber = binding.edtSearch.text.toString()
                Log.d("SEARCH", phoneNumber)
                viewModel.searchUser(phoneNumber)
                true
            } else {
                false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect { user ->
                if (user != null) {
                    binding.layoutResult.visibility = View.VISIBLE
                    binding.tvDisplayName.text = user.displayName
                }
            }
        }

        binding.layoutResult.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchContactFragment_to_chatFragment,
                args = bundleOf(
                    "conversationId" to viewModel.getConversationId(),
                    "otherId" to viewModel.getOtherId()
                    )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}