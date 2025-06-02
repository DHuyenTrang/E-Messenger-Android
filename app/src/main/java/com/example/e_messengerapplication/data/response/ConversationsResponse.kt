package com.example.e_messengerapplication.data.response

import com.example.e_messengerapplication.domain.Conversation
import com.example.e_messengerapplication.domain.Participant

data class ConversationsResponse(
    val code: Int,
    val message: String,
    val result: List<ConversationDto>
) {
}

data class ConversationResponse(
    val code: Int,
    val message: String,
    val result: ConversationDto
)

data class ConversationDto(
    val id: String?,
    val conversationName: String?,
    val lastMessage: String?,
    val lastMessageTime: String,
    val lastActorName: String?,
    val avatarUrl: String?,
    val lastActorId: String?,
    val participantIds: List<ParticipantDto>?
) {
    fun mapToConversation(): Conversation {
        return Conversation(
            id ?: "",
            conversationName ?: "",
            lastMessage ?: "",
            lastMessageTime ?: "",
            lastActorName ?: "",
            participantIds?.map { it.mapToParticipant() } ?: emptyList(),
            avatarUrl ?: "",
            lastActorId ?: ""
        )
    }
}

data class ParticipantDto(
    val participantId: String,
    val role: String,
    val jointAt: String
) {
    fun mapToParticipant(): Participant {
        return Participant(participantId ?: "", role ?: "")
    }
}