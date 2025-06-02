package com.example.e_messengerapplication.data.request

data class UpdateUserRequest(
    val email: String,
    val displayName: String,
    val bio: String,
    val phoneNumber: String,
) {
}