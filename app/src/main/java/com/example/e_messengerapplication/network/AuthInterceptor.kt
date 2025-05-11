package com.example.e_messengerapplication.network

import android.util.Log
import com.example.e_messengerapplication.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authApiService: AuthAPIService
) : Interceptor {

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = tokenManager.getAccessToken()
        val refreshToken = tokenManager.getRefreshToken()

        val originalRequest = chain.request()
        val requestWithAccess = originalRequest.newBuilder()
            .apply {
                if (!accessToken.isNullOrEmpty()) {
                    addHeader("Authorization", "Bearer $accessToken")
                }
            }
            .build()

        var response = chain.proceed(requestWithAccess)

        // Nếu 401, thử refresh token
        if (response.code() == 401 && !refreshToken.isNullOrEmpty()) {
//            response.close()
            try {
                val refreshResponse = authApiService
                    .refreshToken("Bearer $refreshToken")

                if (refreshResponse.isSuccessful) {
                    val newTokens = refreshResponse.body()
                    val newAccessToken = newTokens?.result?.accessToken
                    val newRefreshToken = newTokens?.result?.refreshToken
                    Log.d("Auth", "newAccessToken: $newAccessToken")
                    if (!newAccessToken.isNullOrEmpty()) {
                        if (newRefreshToken != null) {
                            tokenManager.saveToken(newAccessToken, newRefreshToken)
                        }

                        val newRequest = originalRequest.newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer $newAccessToken")
                            .build()

                        return chain.proceed(newRequest)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Token refresh thất bại
            }
        }

        return response
    }
}
