package com.example.e_messengerapplication.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.TokenManager
import com.example.e_messengerapplication.data.response.UserDto
import com.example.e_messengerapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchContactViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private var _user = MutableStateFlow<UserDto?>(null)
    val user: StateFlow<UserDto?> = _user.asStateFlow()

    fun searchUser(phoneNumber: String) {
        viewModelScope.launch {
            val response = userRepository.searchUser(phoneNumber)
            if (response.isSuccessful) {
                _user.value = response.body()?.result
                Log.d("USER", "${response.body()}")
            } else {
                Log.d("USER", "Failed to search user: ${response.code()}")
            }
        }
    }

    fun getConversationId(): String {
        val userId = tokenManager.getUserID()
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

    fun getOtherId(): String {
        val receiverId = _user.value?.id
        return receiverId ?: ""
    }
}