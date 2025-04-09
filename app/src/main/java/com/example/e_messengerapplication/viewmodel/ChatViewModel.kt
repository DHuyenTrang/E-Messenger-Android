package com.example.e_messengerapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.data.request.MessageRequest
import com.example.e_messengerapplication.domain.Message
import com.example.e_messengerapplication.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
): ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    fun fetchMessages(conversationId: String) {
        viewModelScope.launch {
            val response = messageRepository.fetchMessage(conversationId)
            if (response.isSuccessful) {
                _messages.value = response.body()?.result?.map { it.mapToMessage() } ?: emptyList()
            }
        }
    }

    fun sendMessage(otherId: String, text: String) {
        viewModelScope.launch {
            val response = messageRepository.sendMessage(otherId, MessageRequest(text))
            if (response.isSuccessful) {
                Log.d("SEND MESSAGE", "Success")
            }
            else{
                Log.d("SEND MESSAGE", "Failed ${response.code()}")
            }
        }
    }
}