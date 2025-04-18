package com.example.e_messengerapplication.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.databinding.FragmentHomeBinding
import com.example.e_messengerapplication.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.listItem
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ConversationAdapter()

        binding.btnAddContact.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchContactFragment)
        }

        viewModel.getConversations()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.conversations.collect { conversations ->
                if (conversations.isEmpty()) {
                    binding.imgEmptyChat.visibility = View.VISIBLE
                    binding.listItem.visibility = View.GONE
                }
                else {
                    binding.imgEmptyChat.visibility = View.GONE
                    binding.listItem.visibility = View.VISIBLE
                    (recyclerView.adapter as ConversationAdapter).submitList(conversations)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}