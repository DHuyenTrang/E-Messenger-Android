package com.example.e_messengerapplication.di

import com.example.e_messengerapplication.TokenManager
import com.example.e_messengerapplication.data.websocket.WebSocketService
import com.example.e_messengerapplication.network.APIService
import com.example.e_messengerapplication.network.AuthAPIService
import com.example.e_messengerapplication.network.AuthInterceptor
import com.example.e_messengerapplication.utils.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideWebSocketService(tokenManager: TokenManager): WebSocketService {
        return WebSocketService(tokenManager)
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideRetrofitWithClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("noAuth")
    fun provideRetrofitWithoutClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(@Named("auth") retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthAPIService(@Named("noAuth") retrofit: Retrofit): AuthAPIService {
        return retrofit.create(AuthAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }
}