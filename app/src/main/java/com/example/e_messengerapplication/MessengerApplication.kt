package com.example.e_messengerapplication

import android.app.Application
import com.example.e_messengerapplication.data.websocket.WebSocketService
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltAndroidApp
class MessengerApplication: Application() {
    @Inject
    lateinit var webSocketService: WebSocketService

    override fun onCreate() {
        super.onCreate()
        webSocketService.toString()
    }
}
/*
netstat -aon | findstr :8080
taskkill /PID <PID> /F
 */