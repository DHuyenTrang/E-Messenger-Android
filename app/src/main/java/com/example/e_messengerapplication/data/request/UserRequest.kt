package com.example.e_messengerapplication.data.request

data class UserRequest(
    val phoneNumber: String,
    val password: String,
    val dob: String,
    val displayName: String,
    val bio: String,
) {

}