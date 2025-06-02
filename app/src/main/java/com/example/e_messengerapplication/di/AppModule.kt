package com.example.e_messengerapplication.di

import android.app.Application
import android.content.Context
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.data.websocket.WebSocketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): AppStore {
        return AppStore(context)
    }

    @Provides
    @Singleton
    fun provideWebSocketService(app: Application, appStore: AppStore): WebSocketService {
        return WebSocketService(app, appStore)
    }
}