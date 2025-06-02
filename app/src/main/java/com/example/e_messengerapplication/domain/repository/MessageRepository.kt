package com.example.e_messengerapplication.domain.repository

import com.example.e_messengerapplication.domain.Message
import kotlinx.coroutines.flow.SharedFlow
import java.io.File

interface MessageRepository {
    suspend fun fetchMessage(conversationId: String): List<Message>
    suspend fun getUrlResource(file: File, type: String): String
}