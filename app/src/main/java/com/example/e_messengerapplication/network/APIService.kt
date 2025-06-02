package com.example.e_messengerapplication.network

import com.example.e_messengerapplication.data.request.CreateGroupRequest
import com.example.e_messengerapplication.data.request.MessageRequest
import com.example.e_messengerapplication.data.request.UpdateUserRequest
import com.example.e_messengerapplication.data.request.UserRequest
import com.example.e_messengerapplication.data.response.ConversationResponse
import com.example.e_messengerapplication.data.response.ConversationsResponse
import com.example.e_messengerapplication.data.response.MessageResponse
import com.example.e_messengerapplication.data.response.SendMessageResponse
import com.example.e_messengerapplication.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface APIService {
    @POST("group")
    suspend fun createGroup(
        @Body() group: CreateGroupRequest
    ): Response<ConversationsResponse>

    @GET("users/my-info")
    suspend fun getMyInfo(): Response<UserResponse>

    @GET("users/{userId}")
    suspend fun getOtherInfo(
        @Path("userId") userId: String
    ): Response<UserResponse>

    @Multipart
    @PUT("users/avatars")
    suspend fun updateAvatar(
        @Part avatar: MultipartBody.Part
    ): Response<UserResponse>

    @PUT("users")
    suspend fun updateUser(
        @Body() user: UpdateUserRequest
    ): Response<UserResponse>

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
        @Query("pageNum") pageNum: Int = 0,
        @Query("pageSize") pageSize: Int = 10,
    ): Response<MessageResponse>

    @POST("direct/{otherId}")
    suspend fun createDirect(
        @Path("otherId") otherId: String
    ): Response<ConversationResponse>

    @GET("conversations/{conversationId}")
    suspend fun getConversationById(
        @Path("conversationId") conversationId: String
    ): Response<ConversationResponse>

    @Multipart
    @POST("files")
    suspend fun getUrlResource(
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>
}