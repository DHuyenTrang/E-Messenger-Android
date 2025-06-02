package com.example.e_messengerapplication.ui.chat

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.data.websocket.WebSocketService
import com.example.e_messengerapplication.domain.Message
import com.example.e_messengerapplication.data.repository.MessageRepositoryImpl
import com.example.e_messengerapplication.utils.Constant
import com.example.e_messengerapplication.utils.Constant.TAG_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val webSocketService: WebSocketService,
    private val messageRepository: MessageRepositoryImpl
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private var currentConversationId: String? = null
    private var isConnected = false

    fun sendImage(context: Context, conversationId: String, uri: Uri) {
        val file = Constant.uriToFile(context, uri)
        viewModelScope.launch {
            val url = messageRepository.getUrlResource(file!!, "IMAGE")
            Log.d(TAG_MESSAGE, "Resource uploaded: $url")
            if (url != "") {
                webSocketService.sendResource(conversationId, url, "IMAGE")
            }
        }
    }

    fun sendAudio(conversationId: String, file: File) {
        viewModelScope.launch {
            val url = messageRepository.getUrlResource(file, "AUDIO")
            Log.d(TAG_MESSAGE, "Resource uploaded: $url")
            if (url != "") {
                webSocketService.sendResource(conversationId, url, "AUDIO")
            }
        }
    }

    fun fetchMessage(conversationId: String) {
        viewModelScope.launch {
            _messages.value = messageRepository.fetchMessage(conversationId)
            currentConversationId = conversationId
        }
        initWebSocketCollector()
    }

    private fun initWebSocketCollector() {
        if (isConnected) return
        isConnected = true
        viewModelScope.launch {
            webSocketService.messageFlow.collect { message ->
                Log.d(TAG_MESSAGE, message.toString())
                if (message.conversationId == currentConversationId) {
                    _messages.update { it + message }
                }
            }
        }
    }

    fun sendMessage(conversationId: String, text: String) {
        viewModelScope.launch {
            webSocketService.sendMessage(conversationId, text)
        }
    }
}
