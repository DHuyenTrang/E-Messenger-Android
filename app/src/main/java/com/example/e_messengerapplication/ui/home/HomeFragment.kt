package com.example.e_messengerapplication.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.databinding.FragmentHomeBinding
import com.example.e_messengerapplication.domain.Conversation
import com.example.e_messengerapplication.ui.MainActivity
import com.example.e_messengerapplication.ui.profile.SharedUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    val userViewModel: SharedUserViewModel by activityViewModels()

    @Inject
    lateinit var appStore: AppStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setSelectedBottomMenuItem(R.id.homeFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.loadUser()
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.user.collectLatest { user ->
                var url = user?.avatarUrl
                if (url == null || url == "") url = "http://res.cloudinary.com/dtvuu5b2g/image/upload/v1746720818/other_default_avatar_fif3lm.jpg"
                val secureUrl = url.replace("http://", "https://")
                Glide.with(binding.root.context)
                    .load(secureUrl)
                    .into(binding.imageViewAvatar)
            }
        }
        val recyclerView = binding.listItem
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ConversationAdapter(appStore = appStore){
            onConversationClick(it)
        }

        binding.btnAddContact.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchContactFragment)
        }

        viewModel.getConversations()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.conversations.collect { conversations ->
                if (conversations.isEmpty()) {
                    binding.layoutEmptyConversation.visibility = View.VISIBLE
                    binding.listItem.visibility = View.GONE
                }
                else {
                    binding.layoutEmptyConversation.visibility = View.GONE
                    binding.listItem.visibility = View.VISIBLE
                    (recyclerView.adapter as ConversationAdapter).submitList(conversations)
                }
            }
        }
    }

    private fun onConversationClick(conversation: Conversation) {
        findNavController().navigate(
            R.id.action_homeFragment_to_chatFragment,
            args = bundleOf(
                "conversationId" to conversation.id,
                "conversationName" to conversation.name,
                "conversationAvatar" to conversation.avatarUrl
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}