package com.example.e_messengerapplication.domain

data class Conversation(
    val id: String?,
    val name: String?,
    val lastMessage: String?,
    val lastMessageTime: String?,
    val lastSenderName: String?,
    val participantIds: List<String>?
) {
}