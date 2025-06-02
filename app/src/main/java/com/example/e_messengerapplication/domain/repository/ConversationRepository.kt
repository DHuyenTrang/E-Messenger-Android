package com.example.e_messengerapplication.domain.repository

import com.example.e_messengerapplication.domain.Conversation

interface ConversationRepository {
    suspend fun getAllConversation(): List<Conversation>
    suspend fun createDirectConversation(otherId: String): Conversation?
    suspend fun createGroupConversation(name: String, participantIds: List<String>): Conversation?
    suspend fun getAllGroups(): List<Conversation>
    suspend fun getConversation(conversationId: String): Boolean
}