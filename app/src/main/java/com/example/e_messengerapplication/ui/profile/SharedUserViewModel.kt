package com.example.e_messengerapplication.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.data.repository.UserRepositoryImpl
import com.example.e_messengerapplication.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedUserViewModel @Inject constructor(
    private val appStore: AppStore,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {
    private var _fetchSuccess = MutableStateFlow(false)
    val fetchSuccess = _fetchSuccess.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    fun loadUser() {
        _user.value = appStore.getProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val result = userRepository.getMyProfile()
                _fetchSuccess.value = true
                _user.value = result
                Log.d("HomeViewModel", "Profile: $result")
            }
            catch (e: Exception) {
                Log.d("HomeViewModel", "Error: ${e.message}")
            }
        }
    }
}
