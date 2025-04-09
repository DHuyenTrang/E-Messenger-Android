package com.example.e_messengerapplication.repository

import com.example.e_messengerapplication.network.APIService
import javax.inject.Inject

class ConversationRepository @Inject constructor(private val apiService: APIService) {
    suspend fun getAllConversations() = apiService.getAllConversations()
}