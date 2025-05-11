package com.example.e_messengerapplication.ui.chat

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_messengerapplication.TokenManager
import com.example.e_messengerapplication.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val conversationId = requireArguments().getString("conversationId") ?: ""
        val otherId = requireArguments().getString("otherId") ?: ""
        val conversationName = requireArguments().getString("conversationName") ?: ""
        binding.tvDisplayName.text = conversationName

        val recyclerView = binding.listMessages
        val adapter = MessagesAdapter(tokenManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.fetchMessage(conversationId)
        viewModel.connect(conversationId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collectLatest{ messages ->
                adapter.submitList(messages)
                recyclerView.scrollToPosition(0)
            }
        }
        setTextViewFocus()

        binding.btnSendMessage.setOnClickListener {
            val message = binding.edtMessage.text.toString()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(conversationId, message)
                binding.edtMessage.text.clear()
            }
            binding.edtMessage.clearFocus()
            binding.edtMessage.text = null
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setTextViewFocus() {
        val edt_message = binding.edtMessage
        edt_message.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Tăng chiều rộng của EditText
                val params = edt_message.layoutParams as ConstraintLayout.LayoutParams
                params.matchConstraintPercentWidth = 0.8f // Tăng chiều rộng lên 90%
                params.horizontalBias = 0f
                edt_message.layoutParams = params

                // Ẩn các nút gửi hình ảnh và âm thanh
                binding.btnSendImage.visibility = View.GONE
                binding.btnSendVoice.visibility = View.GONE
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}