package com.example.e_messengerapplication.ui.chat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.data.request.MessageRequest
import com.example.e_messengerapplication.data.websocket.WebSocketService
import com.example.e_messengerapplication.domain.Message
import com.example.e_messengerapplication.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val webSocketService: WebSocketService,
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    fun fetchMessage(conversationId: String) {
        Log.d("ChatViewModel", "Fetching messages for conversation: $conversationId")
        viewModelScope.launch {
            val response = messageRepository.fetchMessage(conversationId)
            if (response.isSuccessful) {
                val messageList = response.body()?.result?.map { it.mapToMessage() } ?: emptyList()
                _messages.value = messageList
                Log.d("ChatViewModel", "Fetched messages: ${messageList.first()}")
            }
            else {
                Log.e("ChatViewModel", "Failed to fetch messages: ${response.code()} - ${response.errorBody()?.string()}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun connect(conversationId: String) {

        viewModelScope.launch {
            Log.d("ChatViewModel", "Connecting to WebSocket")
            webSocketService.messageFlow.collectLatest { msg ->
                _messages.update { it + msg }
                Log.d("ChatViewModel", "Received message: $msg")
            }
        }
        webSocketService.connect(conversationId)
    }

    fun sendMessage(conversationId: String, text: String) {
        viewModelScope.launch {
            webSocketService.sendMessage(conversationId, text)
        }
    }
}

