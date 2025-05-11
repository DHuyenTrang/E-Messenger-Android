package com.example.e_messengerapplication.network

import com.example.e_messengerapplication.data.request.MessageRequest
import com.example.e_messengerapplication.data.response.ConversationsResponse
import com.example.e_messengerapplication.data.response.MessageResponse
import com.example.e_messengerapplication.data.response.SendMessageResponse
import com.example.e_messengerapplication.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("conversations/all")
    suspend fun getAllConversations(
        @Query("pageNum") pageNum: Int = 0,
        @Query("pageSize") pageSize: Int = 10,
    ): Response<ConversationsResponse>

    @GET("users/{identifier}")
    suspend fun searchUser(
        @Path("identifier") phoneNumber: String
    ): Response<UserResponse>

    @GET("chat/histories/{conversationId}")
    suspend fun fetchMessages(
        @Path("conversationId") conversationId: String,
    ):Response<MessageResponse>

    @POST("direct/{otherId}")
    suspend fun createDirect(
        @Path("otherId") otherId: String
    ):Response<SendMessageResponse>

}