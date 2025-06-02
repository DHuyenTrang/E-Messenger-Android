package com.example.e_messengerapplication.domain.repository

import com.example.e_messengerapplication.domain.User
import java.io.File

interface UserRepository {
    suspend fun getMyProfile(): User
    suspend fun getOtherProfile(userId: String): User
    suspend fun updateProfile(phoneNumber: String, email: String, displayName: String, bio: String): User
    suspend fun updateAvatar(avatar: File): String
    suspend fun searchUser(phoneNumber: String): User
    suspend fun register(name: String, phoneNumber: String, password: String): User
}