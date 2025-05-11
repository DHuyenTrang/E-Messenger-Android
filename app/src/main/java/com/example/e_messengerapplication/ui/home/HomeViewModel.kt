package com.example.e_messengerapplication.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.TokenManager
import com.example.e_messengerapplication.domain.Conversation
import com.example.e_messengerapplication.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val tokenManager: TokenManager
): ViewModel() {
    private var _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: MutableStateFlow<List<Conversation>> = _conversations

    fun getConversations() {
        Log.d("CONVERSATION", "getConversations")
        viewModelScope.launch {
            val response = conversationRepository.getAllConversations()
            if (response.isSuccessful) {
                _conversations.value =
                    response.body()?.result?.map { it.mapToConversation() } ?: emptyList()
                Log.d("CONVERSATION", "${response.body()}")
            } else {
                Log.d("CONVERSATION", "Failed to get conversations: ${response.code()}")
            }
        }
    }

    fun getOtherId(conversation: Conversation): String {
        val participantIds = conversation.participantIds
        val userId = tokenManager.getUserID()
        if (participantIds != null && userId != null) {
            participantIds.forEach {
                if (it != userId) {
                    return it
                }
            }
        }
        return ""
    }
}