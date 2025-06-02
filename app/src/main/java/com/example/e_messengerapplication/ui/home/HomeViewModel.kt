package com.example.e_messengerapplication.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.domain.Conversation
import com.example.e_messengerapplication.data.repository.ConversationRepositoryImpl
import com.example.e_messengerapplication.data.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val conversationRepository: ConversationRepositoryImpl,
): ViewModel() {
    private var _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: MutableStateFlow<List<Conversation>> = _conversations

    fun getConversations() {
        viewModelScope.launch {
            val result = conversationRepository.getAllConversation()
            _conversations.value = result
        }
    }

}