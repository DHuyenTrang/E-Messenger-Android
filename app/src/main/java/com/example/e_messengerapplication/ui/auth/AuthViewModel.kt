package com.example.e_messengerapplication.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.data.request.AuthRequest
import com.example.e_messengerapplication.data.repository.AuthRepository
import com.example.e_messengerapplication.data.websocket.WebSocketService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appStore: AppStore,
): ViewModel() {
    @Inject lateinit var webSocketService: WebSocketService
    private var _isLoginSuccess = MutableStateFlow<Boolean?>(null)
    val isLoginSuccess: StateFlow<Boolean?> = _isLoginSuccess.asStateFlow()

    fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            val authRequest = AuthRequest(phoneNumber, password)

            val response = authRepository.login(authRequest)
            if (response.isSuccessful) {
                Log.d("AUTH", "${response.body()}")
                _isLoginSuccess.value = true

                val accessToken = response.body()?.result?.accessToken
                val refreshToken = response.body()?.result?.refreshToken
                val id = response.body()?.result?.userId

                appStore.saveToken(accessToken!!, refreshToken!!)
                appStore.saveUserID(id!!)

                webSocketService.disconnect()
                webSocketService.connect()
            } else {
                _isLoginSuccess.value = false
                Log.d("AUTH", "Failed to login: ${response.code()}")
            }
        }
    }

    fun logout() {
        Log.d("AUTH", "Logout")
        appStore.clearTokens()
    }
}