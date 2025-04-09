package com.example.e_messengerapplication.repository

import com.example.e_messengerapplication.data.request.AuthRequest
import com.example.e_messengerapplication.data.response.AuthResponse
import com.example.e_messengerapplication.network.APIService
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: APIService
) {
    suspend fun login(authRequest: AuthRequest): Response<AuthResponse> {
        return apiService.login(authRequest)
    }
}