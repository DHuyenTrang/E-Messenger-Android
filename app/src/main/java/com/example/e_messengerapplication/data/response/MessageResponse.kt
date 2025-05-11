package com.example.e_messengerapplication.data.response

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.e_messengerapplication.domain.Message
import com.example.e_messengerapplication.utils.Constant

data class MessageResponse(
    val result: List<MessageDto>
) {
}

data class MessageDto(
    val content: String?,
    val type: String?,
    val senderId: String?,
    val sentAt: String,
    val senderName: String
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapToMessage(): Message {
        return Message(
            text = content ?: "",
            senderId = senderId ?: "unknown",
            sentAt = Constant.formatMessageTime(sentAt)
        )
    }
}