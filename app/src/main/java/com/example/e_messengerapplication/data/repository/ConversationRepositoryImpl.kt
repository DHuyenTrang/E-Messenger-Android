package com.example.e_messengerapplication.data.repository

import android.util.Log
import com.example.e_messengerapplication.domain.Conversation
import com.example.e_messengerapplication.domain.repository.ConversationRepository
import com.example.e_messengerapplication.network.APIService
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val apiService: APIService
): ConversationRepository {
    override suspend fun getAllConversation(): List<Conversation> {
        val response = apiService.getAllConversations(0, 20)
        if (response.isSuccessful) {
            Log.d("CONVERSATIONS", "Success to fetch conversations: ${response.body()}")
            return response.body()?.result?.map { it.mapToConversation() } ?: emptyList()
        } else {
            Log.d("CONVERSATIONS", "Failed to fetch conversations: ${response.code()}")
            return emptyList()
        }
    }

    override suspend fun createDirectConversation(otherId: String): Conversation? {
        val response = apiService.createDirect(otherId)
        if (response.isSuccessful) {
            Log.d("CONVERSATIONS", "Success to create direct conversation: ${response.body()}")
            return response.body()?.result?.mapToConversation()
        }
        else {
            Log.d("CONVERSATIONS", "Failed to create direct conversation: ${response.code()}")
            return null
        }
    }

    override suspend fun createGroupConversation(
        name: String,
        participantIds: List<String>
    ): Conversation? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllGroups(): List<Conversation> {
        TODO("Not yet implemented")
    }

    override suspend fun getConversation(conversationId: String): Boolean {
        val response = apiService.getConversationById(conversationId)
        if (response.isSuccessful) {
            Log.d("CONVERSATIONS", "Success to fetch conversation: ${response.body()}")
            return true
        }
        else {
            Log.d("CONVERSATIONS", "Failed to fetch conversation: ${response.code()}")
            return false
        }
    }

}