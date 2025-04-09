package com.example.e_messengerapplication.domain

data class Message(
    val text: String,
    val senderId: String,
    val sentAt: String
) {
}