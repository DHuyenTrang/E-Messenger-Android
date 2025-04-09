package com.example.e_messengerapplication.network

import com.example.e_messengerapplication.data.request.AuthRequest
import com.example.e_messengerapplication.data.request.UserRequest
import com.example.e_messengerapplication.data.response.AuthResponse
import com.example.e_messengerapplication.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("users/sign-up")
    suspend fun register(
        @Body userRequest: UserRequest
    ): Response<UserResponse>

    @POST("auth/log-in")
    suspend fun login(
        @Body authRequest: AuthRequest
    ): Response<AuthResponse>

}