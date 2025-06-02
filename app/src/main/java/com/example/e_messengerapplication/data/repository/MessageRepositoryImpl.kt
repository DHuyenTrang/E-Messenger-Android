package com.example.e_messengerapplication.data.repository

import android.util.Log
import com.example.e_messengerapplication.data.response.MessageResponse
import com.example.e_messengerapplication.data.websocket.WebSocketService
import com.example.e_messengerapplication.domain.Message
import com.example.e_messengerapplication.domain.repository.MessageRepository
import com.example.e_messengerapplication.network.APIService
import com.example.e_messengerapplication.utils.Constant.TAG_MESSAGE
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.MediaType
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MessageRepositoryImpl @Inject constructor(
    private val apiService: APIService
): MessageRepository {
    override suspend fun fetchMessage(conversationId: String): List<Message> {
        val response = apiService.fetchMessages(conversationId)
        if (response.isSuccessful) {
            Log.d(TAG_MESSAGE, "Successfully fetched messages: ${response.body()}")
            return response.body()?.result?.map { it.mapToMessage() } ?: emptyList()
        } else {
            Log.d(TAG_MESSAGE, "Failed to fetch messages: ${response.code()}")
            return emptyList()
        }
    }

    override suspend fun getUrlResource(file: File, type: String): String {
        val requestBody = when(type) {
            "IMAGE" -> RequestBody.create(MediaType.parse("image/jpeg"), file)
            "AUDIO" -> RequestBody.create(MediaType.parse("audio/mpeg"), file)
            else -> RequestBody.create(MediaType.parse("image/jpeg"), file)
        }
        val body = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val response = apiService.getUrlResource(body)

        if (response.isSuccessful) {
            Log.d(TAG_MESSAGE, "Successfully fetched url resource: ${response.body()}")
            return response.body()?.string() ?: ""
        } else {
            Log.d(TAG_MESSAGE, "Failed to fetch url resource: ${response.code()}")
            return ""
        }
    }

}