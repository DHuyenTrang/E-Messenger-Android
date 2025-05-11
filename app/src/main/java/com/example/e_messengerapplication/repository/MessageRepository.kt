package com.example.e_messengerapplication.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.e_messengerapplication.data.request.MessageRequest
import com.example.e_messengerapplication.data.response.MessageResponse
import com.example.e_messengerapplication.data.websocket.WebSocketService
import com.example.e_messengerapplication.network.APIService
import retrofit2.Response
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val apiService: APIService
){

    suspend fun createDirect(otherId: String)
    = apiService.createDirect(otherId)

    suspend fun fetchMessage(conversationId: String): Response<MessageResponse> = apiService.fetchMessages(conversationId)
}