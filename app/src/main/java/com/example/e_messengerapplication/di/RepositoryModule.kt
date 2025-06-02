package com.example.e_messengerapplication.di

import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.data.repository.ConversationRepositoryImpl
import com.example.e_messengerapplication.data.repository.MessageRepositoryImpl
import com.example.e_messengerapplication.data.repository.UserRepositoryImpl
import com.example.e_messengerapplication.data.websocket.WebSocketService
import com.example.e_messengerapplication.domain.repository.ConversationRepository
import com.example.e_messengerapplication.domain.repository.MessageRepository
import com.example.e_messengerapplication.domain.repository.UserRepository
import com.example.e_messengerapplication.network.APIService
import com.example.e_messengerapplication.network.AuthAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: APIService,
        appStore: AppStore,
        authAPIService: AuthAPIService
    ): UserRepository {
        return UserRepositoryImpl(apiService, appStore, authAPIService)
    }

    @Provides
    @Singleton
    fun provideConversationRepository(
        apiService: APIService
    ): ConversationRepository {
        return ConversationRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(
        apiService: APIService
    ): MessageRepository {
        return MessageRepositoryImpl(apiService)
    }

}