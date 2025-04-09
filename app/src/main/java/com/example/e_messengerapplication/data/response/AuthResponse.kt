package com.example.e_messengerapplication.data.response

data class AuthResponse(
    val code: Int,
    val message: String,
    val result: Result
) {
}

data class Result(
    val accessToken: String,
    val refreshToken: String
) {

}