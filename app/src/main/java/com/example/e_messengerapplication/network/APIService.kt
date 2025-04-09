package com.example.e_messengerapplication.network

import com.example.e_messengerapplication.data.request.AuthRequest
import com.example.e_messengerapplication.data.request.MessageRequest
import com.example.e_messengerapplication.data.request.UserRequest
import com.example.e_messengerapplication.data.response.AuthResponse
import com.example.e_messengerapplication.data.response.ConversationsResponse
import com.example.e_messengerapplication.data.response.MessageResponse
import com.example.e_messengerapplication.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @POST("users/sign-up")
    suspend fun register(
        @Body userRequest: UserRequest
    ): Response<UserResponse>

    @POST("auth/log-in")
    suspend fun login(
        @Body authRequest: AuthRequest
    ): Response<AuthResponse>

    @GET("conversation/all")
    suspend fun getAllConversations(): Response<ConversationsResponse>

    @GET("users/find/{phone}")
    suspend fun searchUser(
        @Path("phone") phoneNumber: String
    ): Response<UserResponse>

    @GET("direct/{conversationId}")
    suspend fun fetchMessage(
        @Path("conversationId") conversationId: String,
    ):Response<MessageResponse>

    @POST("direct/{otherId}")
    suspend fun sendMessage(
        @Path("otherId") otherId: String,
        @Body messageRequest: MessageRequest
    ):Response<MessageResponse>
}