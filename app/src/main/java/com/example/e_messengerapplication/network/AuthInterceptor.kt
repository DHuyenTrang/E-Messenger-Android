package com.example.e_messengerapplication.network

import com.example.e_messengerapplication.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = tokenManager.getAccessToken() ?: ""
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(request)
        if (response.code() == 401) {
            // handle unauthorized response
        }

        return response
    }
}