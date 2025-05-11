package com.example.e_messengerapplication.data.request

data class MessageRequest(
    val text: String,
    val type: String = "TEXT"
) {
}