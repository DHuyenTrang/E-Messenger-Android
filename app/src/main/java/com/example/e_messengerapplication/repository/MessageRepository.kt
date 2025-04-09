package com.example.e_messengerapplication.repository

import com.example.e_messengerapplication.data.request.MessageRequest
import com.example.e_messengerapplication.network.APIService
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val apiService: APIService
){
    suspend fun fetchMessage(conversationId: String) = apiService.fetchMessage(conversationId)

    suspend fun sendMessage(otherId: String, messageRequest: MessageRequest) = apiService.sendMessage(otherId, messageRequest)
}