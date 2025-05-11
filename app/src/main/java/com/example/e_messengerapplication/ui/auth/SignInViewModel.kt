package com.example.e_messengerapplication.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.TokenManager
import com.example.e_messengerapplication.data.request.AuthRequest
import com.example.e_messengerapplication.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
): ViewModel() {
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

                tokenManager.saveToken(accessToken!!, refreshToken!!)
                tokenManager.saveUserID(id!!)
            } else {
                _isLoginSuccess.value = false
                Log.d("AUTH", "Failed to login: ${response.code()}")
            }
        }
    }
}