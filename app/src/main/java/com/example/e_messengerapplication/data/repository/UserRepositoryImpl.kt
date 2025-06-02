package com.example.e_messengerapplication.data.repository

import android.util.Log
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.data.request.UpdateUserRequest
import com.example.e_messengerapplication.data.request.UserRequest
import com.example.e_messengerapplication.domain.User
import com.example.e_messengerapplication.domain.repository.UserRepository
import com.example.e_messengerapplication.network.APIService
import com.example.e_messengerapplication.network.AuthAPIService
import com.example.e_messengerapplication.utils.Constant.TAG_USER
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val appStore: AppStore,
    private val authAPIService: AuthAPIService,
): UserRepository {
    override suspend fun getMyProfile(): User {
        val response = apiService.getMyInfo()
        if (response.isSuccessful) {
            Log.d(TAG_USER, "Success to get my profile: ${response.body()}")
            val user = response.body()?.result?.mapToUser()
            if (user != null) {
                appStore.saveProfile(user)
            }
            return user ?: throw Exception("Failed to map user")
        }
        else {
            Log.d(TAG_USER, "Failed to get my profile: ${response.code()}")
            throw Exception("Failed to get my profile: ${response.code()}")
        }
    }

    override suspend fun getOtherProfile(userId: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(phoneNumber: String, email: String, displayName: String, bio: String): User {
        val userRequest = UpdateUserRequest(
            phoneNumber = phoneNumber,
            email = email,
            displayName = displayName,
            bio = bio
        )
        val response = apiService.updateUser(userRequest)
        if (response.isSuccessful) {
            val newUser = response.body()?.result?.mapToUser()
            if (newUser != null) {
                appStore.saveProfile(newUser)
                return newUser
            } else {
                throw Exception("Failed to map user")
            }
        }
        else {
            Log.d(TAG_USER, "Failed to update profile: ${response.code()}")
            throw Exception("Failed to update profile: ${response.code()}")
        }
    }

    override suspend fun updateAvatar(avatar: File): String {
        val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), avatar)
        val body = MultipartBody.Part.createFormData("avatar", avatar.name, requestBody)

        val response = apiService.updateAvatar(body)
        if (response.isSuccessful) {
            Log.d(TAG_USER, "Success to update avatar: ${response.body()}")
            response.body()?.result?.avatarUrl?.let { appStore.saveAvatar(it) }
            return response.body()?.result?.avatarUrl ?: throw Exception("Failed to get avatar url")
        }
        else {
            Log.d(TAG_USER, "Failed to update avatar: ${response.code()}")
            throw Exception("Failed to update avatar: ${response.code()}")
        }
    }

    override suspend fun searchUser(phoneNumber: String): User {
        val response = apiService.searchUser(phoneNumber)
        if (response.isSuccessful) {
            Log.d(TAG_USER, "Success to search user: ${response.body()}")
            return response.body()?.result?.mapToUser() ?: throw Exception("Failed to map user")
        } else {
            Log.d(TAG_USER, "Failed to search user: ${response.code()}")
            throw Exception("Failed to search user: ${response.code()}")
        }
    }

    override suspend fun register(name: String, phoneNumber: String, password: String): User {
        val request = UserRequest(
            phoneNumber = phoneNumber,
            password = password,
            dob = "",
            email = "",
            gender = "",
            displayName = name,
            bio = ""
        )
        val response = authAPIService.register(request)
        if (response.isSuccessful) {
            Log.d(TAG_USER, "Success to register: ${response.body()}")
            return response.body()?.result?.mapToUser() ?: throw Exception("Failed to map user")
        }
        else {
            Log.d(TAG_USER, "Failed to register: ${response.code()}")
            throw Exception("Failed to register: ${response.code()}")
        }
    }

}