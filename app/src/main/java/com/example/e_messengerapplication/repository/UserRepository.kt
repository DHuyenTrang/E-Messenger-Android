package com.example.e_messengerapplication.repository

import com.example.e_messengerapplication.network.APIService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: APIService,
) {
    suspend fun searchUser(phoneNumber: String) = apiService.searchUser(phoneNumber)
}