package com.example.e_messengerapplication.data.response

import com.example.e_messengerapplication.domain.Conversation

data class ConversationsResponse(
    val result: List<ConversationDto>
) {
}

data class ConversationDto(
    val id: String?,
    val conversationName: String?,
    val lastMessage: String?,
    val lastMessageTime: String?,
    val lastSenderName: String?,
    val participantIds: List<String>?
) {
    fun mapToConversation(): Conversation {
        return Conversation(id, conversationName, lastMessage, lastMessageTime, lastSenderName, participantIds)
    }
}