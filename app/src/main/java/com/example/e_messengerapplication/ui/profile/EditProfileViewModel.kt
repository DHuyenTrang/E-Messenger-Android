package com.example.e_messengerapplication.ui.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_messengerapplication.data.repository.AuthRepository
import com.example.e_messengerapplication.data.repository.UserRepositoryImpl
import com.example.e_messengerapplication.domain.User
import com.example.e_messengerapplication.domain.repository.UserRepository
import com.example.e_messengerapplication.utils.Constant
import com.example.e_messengerapplication.utils.Constant.TAG_USER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl
): ViewModel() {
    private val _updateSuccess = MutableStateFlow<Boolean>(false)
    val updateSuccess = _updateSuccess.asStateFlow()

    fun updateAvatar(context: Context, uri: Uri) {
        val file = Constant.uriToFile(context, uri)
        viewModelScope.launch {
            userRepository.updateAvatar(file!!)
            _updateSuccess.value = true
        }
    }
    fun updateProfile(name: String, email: String, bio: String, phoneNumber: String) {
        viewModelScope.launch {
            try {
                userRepository.updateProfile(phoneNumber, email, name, bio)
                _updateSuccess.value = true
            }
            catch (e: Exception) {
                Log.d(TAG_USER, "Error: ${e.message}")
            }
        }
    }
}