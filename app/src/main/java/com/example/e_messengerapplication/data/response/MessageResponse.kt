package com.example.e_messengerapplication.data.response

import com.example.e_messengerapplication.domain.Message
import com.example.e_messengerapplication.domain.MessageType

data class MessageResponse(
    val result: List<MessageDto>
)

data class MessageDto(
    val content: String?,
    val type: String?,
    val actorId: String?,
    val time: String,
    val actorName: String,
    val actorAvatarUrl: String?,
    val conversationId: String,
    val mediaType: String?,
    val url: String?
) {
    fun mapToMessage(): Message {
        val resolvedType = when (type) {
            "MEDIA" -> {
                when (mediaType) {
                    "IMAGE" -> MessageType.IMAGE
                    "AUDIO" -> MessageType.AUDIO
                    else -> MessageType.TEXT
                }
            }
            "TEXT" -> MessageType.TEXT
            else -> MessageType.TEXT
        }

        val messageContent = when (resolvedType) {
            MessageType.IMAGE, MessageType.AUDIO -> url ?: ""
            MessageType.TEXT -> content ?: ""
        }

        return Message(
            content = messageContent,
            type = resolvedType,
            actorId = actorId.orEmpty(),
            time = time,
            actorName = actorName,
            actorAvatarUrl = actorAvatarUrl.orEmpty(),
            conversationId = conversationId
        )
    }
}
