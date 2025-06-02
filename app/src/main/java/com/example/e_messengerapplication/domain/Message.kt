package com.example.e_messengerapplication.domain

data class Message(
    val actorId: String,
    val actorName: String,
    val actorAvatarUrl: String,
    val content: String,
    val type: MessageType,
    val time: String,
    val conversationId: String,
) {
}

enum class MessageType {
    TEXT,
    IMAGE,
    AUDIO
}