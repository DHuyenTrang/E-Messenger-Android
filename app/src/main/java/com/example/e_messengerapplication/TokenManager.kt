package com.example.e_messengerapplication

import android.content.Context
import android.content.SharedPreferences
import com.example.e_messengerapplication.data.response.UserResponse
import javax.inject.Inject

class TokenManager @Inject constructor(context: Context) {
    private val authPrefs: SharedPreferences = context.getSharedPreferences("authPrefs", Context.MODE_PRIVATE)
    private val userPrefs: SharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

    fun saveUser(userResponse: UserResponse) {
        val editor = userPrefs.edit()

        editor.apply()
    }

    fun saveToken(accessToken: String, refreshToken: String) {
        val editor = authPrefs.edit()
        editor.putString("accessToken", accessToken)
        editor.putString("refreshToken", refreshToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return authPrefs.getString("accessToken", null)
    }

    fun getRefreshToken(): String? {
        return authPrefs.getString("refreshToken", null)
    }

    fun clearTokens() {
        authPrefs.edit().clear().apply()
    }
}