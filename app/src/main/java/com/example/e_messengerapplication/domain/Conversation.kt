package com.example.e_messengerapplication.domain

data class Conversation(
    val id: String?,
    val name: String?,
    val lastMessage: String?,
    val lastMessageTime: String?,
    val lastActorName: String?,
    val participantIds: List<Participant>,
    val avatarUrl: String?,
    val lastActorId: String?
) {
}

data class Participant(
    val participantId: String,
    val role: String,
)