package com.example.e_messengerapplication.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_messengerapplication.TokenManager
import com.example.e_messengerapplication.databinding.FragmentChatBinding
import com.example.e_messengerapplication.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val conversationId = requireArguments().getString("conversationId") ?: ""
        val otherId = requireArguments().getString("otherId") ?: ""

        val recyclerView = binding.listMessages
        val adapter = MessagesAdapter(tokenManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)

        viewModel.fetchMessages(conversationId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                adapter.submitList(messages)
            }
        }
        setTextViewFocus()

        binding.btnSendMessage.setOnClickListener {
            val message = binding.edtMessage.text.toString()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(otherId, message)
                binding.edtMessage.text.clear()
            }
            binding.edtMessage.clearFocus()
            binding.edtMessage.text = null
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
//            } else {
//                // Đặt lại chiều rộng ban đầu
//                val params = edt_message.layoutParams as ConstraintLayout.LayoutParams
//                params.matchConstraintPercentWidth = 0.6f // Trở về 70%
//                edt_message.layoutParams = params

//                // Hiển thị lại các nút
//                binding.btnSendImage.visibility = View.VISIBLE
//                binding.btnSendVoice.visibility = View.VISIBLE
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}