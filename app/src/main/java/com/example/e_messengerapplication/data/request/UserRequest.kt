package com.example.e_messengerapplication.data.request

data class UserRequest(
    val phoneNumber: String,
    val password: String,
    val dob: String,
    val email: String,
    val gender: String,
    val displayName: String,
    val bio: String,
) {

}