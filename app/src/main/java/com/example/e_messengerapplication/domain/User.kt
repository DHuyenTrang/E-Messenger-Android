package com.example.e_messengerapplication.domain

data class User(
    val id: String,
    val phoneNumber: String,
    val dob: String,
    val displayName: String,
    val email: String,
    val avatarUrl: String,
    val bio: String,
    val fcmToken: String
) {
}