package com.example.e_messengerapplication.repository

import com.example.e_messengerapplication.data.request.AuthRequest
import com.example.e_messengerapplication.data.response.AuthResponse
import com.example.e_messengerapplication.network.APIService
import com.example.e_messengerapplication.network.AuthAPIService
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthAPIService
) {
    suspend fun login(authRequest: AuthRequest): Response<AuthResponse> {
        return authApiService.login(authRequest)
    }
}