package com.example.e_messengerapplication

import android.content.Context
import android.content.SharedPreferences
import com.example.e_messengerapplication.domain.User
import javax.inject.Inject

class AppStore @Inject constructor(context: Context) {
    private val authPrefs: SharedPreferences =
        context.getSharedPreferences("authPrefs", Context.MODE_PRIVATE)
    private val userPrefs: SharedPreferences =
        context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
    private val settingPrefs: SharedPreferences =
        context.getSharedPreferences("settingsPrefs", Context.MODE_PRIVATE)

    fun saveAvatar(avatarUrl: String) {
        val editor = userPrefs.edit()
        editor.putString("avatarUrl", avatarUrl)
        editor.apply()
    }
    fun saveProfile(user: User) {
        val editor = userPrefs.edit()
        editor.putString("displayName", user.displayName)
        editor.putString("avatarUrl", user.avatarUrl)
        editor.putString("bio", user.bio)
        editor.putString("phoneNumber", user.phoneNumber)
        editor.putString("email", user.email)
        editor.putString("dob", user.dob)
        editor.putString("fcmToken", user.fcmToken)
        editor.apply()
    }

    fun getProfile(): User {
        val displayName = userPrefs.getString("displayName", null)
        val avatarUrl = userPrefs.getString("avatarUrl", null)
        val bio = userPrefs.getString("bio", null)
        val phoneNumber = userPrefs.getString("phoneNumber", null)
        val email = userPrefs.getString("email", null)
        val dob = userPrefs.getString("dob", null)
        val id = userPrefs.getString("userID", null)
        val fcmToken = userPrefs.getString("fcmToken", null)
        return User(
            id ?: "",
            phoneNumber ?: "",
            dob ?: "",
            displayName ?: "",
            email ?: "",
            avatarUrl ?: "",
            bio ?: "",
            fcmToken ?: ""
        )
    }

    fun saveUserID(id: String) {
        val editor = userPrefs.edit()
        editor.putString("userID", id)
        editor.apply()
    }

    fun getUserID(): String? {
        return userPrefs.getString("userID", null)
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
        userPrefs.edit().clear().apply()
    }
}