package com.example.e_messengerapplication.data.response

data class AuthResponse(
    val result: TokenResponse
) {
}

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
) {

}