package com.example.e_messengerapplication.data.response

import com.example.e_messengerapplication.domain.Message

data class MessageResponse(
    val result: List<MessageDto>
) {
}

data class MessageDto(
    val text: String,
    val senderId: String,
    val sentAt: String,
    val senderName: String
) {
    fun mapToMessage(): Message {
        return Message(text, senderId, sentAt)
    }
}