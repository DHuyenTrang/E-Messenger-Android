package com.example.e_messengerapplication.data.response

import com.example.e_messengerapplication.domain.Conversation

data class ConversationsResponse(
    val result: List<ConversationDto>
) {
}

data class ConversationDto(
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val lastMessageSenderId: String,
) {
    fun mapToConversation(): Conversation {
        return Conversation(id, name, lastMessage, lastMessageTime, lastMessageSenderId)
    }
}