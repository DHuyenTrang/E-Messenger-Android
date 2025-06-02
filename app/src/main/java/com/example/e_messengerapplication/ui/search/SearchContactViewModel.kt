package com.example.e_messengerapplication.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.data.repository.ConversationRepositoryImpl
import com.example.e_messengerapplication.data.repository.UserRepositoryImpl
import com.example.e_messengerapplication.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchContactViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl,
    private val appStore: AppStore,
    private val conversationRepository: ConversationRepositoryImpl
) : ViewModel() {
    private var _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private var _createSuccess = MutableStateFlow(false)
    val createSuccess: StateFlow<Boolean> = _createSuccess.asStateFlow()

    fun searchUser(phoneNumber: String) {
        viewModelScope.launch {
            _user.value = userRepository.searchUser(phoneNumber)
        }
    }

    fun createDirectConversation(){
        viewModelScope.launch {
            val isCreated = conversationRepository.getConversation(getConversationId())
            if (!isCreated) {
                val conversation = conversationRepository.createDirectConversation(getOtherId())
                if (conversation != null) {
                    _createSuccess.value = true
                } else {
                    _createSuccess.value = false
                }
            }
            else {
                _createSuccess.value = true
            }
        }
    }

    fun getConversationId(): String {
        val userId = appStore.getUserID()
        val receiverId = _user.value?.id
        return if (userId != null && receiverId != null) {
            if (userId < receiverId) {
                "$userId-$receiverId"
            } else {
                "$receiverId-$userId"
            }
        } else{
            ""
        }
    }

    private fun getOtherId(): String {
        val receiverId = _user.value?.phoneNumber
        return receiverId ?: ""
    }
}