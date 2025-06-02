package com.example.e_messengerapplication.data.websocket

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.domain.Message
import com.example.e_messengerapplication.utils.Constant.WEBSOCKET_URL
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.json.JSONException
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketService @Inject constructor(
    private val application: Application,
    private val appStore: AppStore
) : DefaultLifecycleObserver {

    private var stompClient: StompClient? = null
    private val compositeDisposable = CompositeDisposable()

    private val _messageFlow = MutableSharedFlow<Message>(replay = 1)
    val messageFlow: SharedFlow<Message> = _messageFlow

    private var isConnected = false

    init {
        Log.d("WebSocketService", "init")
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerNetworkCallback()
    }

    override fun onStart(owner: LifecycleOwner) {
        // App in foreground → connect if has network
        if (isNetworkAvailable()) {
            connect()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        // App in background → disconnect
        disconnect()
    }

    private fun registerNetworkCallback() {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d("WebSocketService", "Network available → reconnect if needed")
                if (!isConnected) connect()
            }

            override fun onLost(network: Network) {
                Log.d("WebSocketService", "Network lost → disconnect")
                disconnect()
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
    }

    fun connect() {
        if (isConnected) return

        val token = appStore.getAccessToken()
        if (token.isNullOrEmpty()) return

        val headers = mapOf("Authorization" to "Bearer $token")
        stompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            WEBSOCKET_URL,
            headers
        ).apply {
            withClientHeartbeat(10000)
            withServerHeartbeat(10000)
        }

        stompClient?.connect()
        stompClient?.lifecycle()?.subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED -> {
                    isConnected = true
                    Log.d("WebSocket", "Connected")
                    onTopicMessage()
                    onTopicConversation()
                }

                LifecycleEvent.Type.ERROR -> {
                    isConnected = false
                    Log.e("WebSocket", "Connection error", event.exception)
                }

                LifecycleEvent.Type.CLOSED -> {
                    isConnected = false
                    Log.d("WebSocket", "Connection closed")
                }

                else -> {}
            }
        }?.let { compositeDisposable.add(it) }
    }

    fun disconnect() {
        if (!isConnected) return

        stompClient?.disconnect()
        compositeDisposable.clear()
        isConnected = false
    }

    @SuppressLint("CheckResult")
    fun sendMessage(conversationId: String, message: String) {
        val destination = "/chat/$conversationId/send-text"
        val payload = JSONObject()
            .put("text", message)
            .toString()

        stompClient?.send(destination, payload)?.subscribe({
            Log.d("WebSocket", "Message sent: $message")
        }, {
            Log.e("WebSocket", "Send error", it)
        })
    }

    @SuppressLint("CheckResult")
    fun sendResource(conversationId: String, resource: String, type: String) {
        val destination = "/chat/$conversationId/send-media"
        val payload = JSONObject()
            .put("uploadedUrl", resource)
            .put("mediaType", type)
            .toString()
        stompClient?.send(destination, payload)?.subscribe({
            Log.d("WebSocket", "Resource sent: $resource")
        }, {
            Log.e("WebSocket", "Send error", it)
        })
    }

    private fun onTopicMessage() {
        stompClient?.topic("/user/messages")
            ?.subscribe({ msg ->
                try {
                    val payload = msg.payload
                    val json = JSONObject(payload)

                    val content = json.optString("content")
                    val actorId = json.optString("actorId")
                    val actorName = json.optString("actorName")
                    val actorAvatarUrl = json.optString("actorAvatarUrl")
                    val conversationId = json.optString("conversationId")
                    val time = json.optString("time")
                    val typeString = json.optString("type") // "TEXT", "MEDIA", etc.
                    val mediaType = json.optString("mediaType") // "IMAGE", "AUDIO", etc.
                    val url = json.optString("url")
                    if (typeString == "MEDIA") {
                        if (mediaType == "IMAGE") {
                            val message = Message(
                                actorId = actorId,
                                actorName = actorName,
                                actorAvatarUrl = actorAvatarUrl,
                                content = url,
                                type = com.example.e_messengerapplication.domain.MessageType.IMAGE,
                                time = time,
                                conversationId = conversationId
                            )
                            _messageFlow.tryEmit(message)
                        }
                        else {
                            val message = Message(
                                actorId = actorId,
                                actorName = actorName,
                                actorAvatarUrl = actorAvatarUrl,
                                content = url,
                                type = com.example.e_messengerapplication.domain.MessageType.AUDIO,
                                time = time,
                                conversationId = conversationId
                            )
                            _messageFlow.tryEmit(message)
                        }
                    }
                    else {
                        val message = Message(
                            actorId = actorId,
                            actorName = actorName,
                            actorAvatarUrl = actorAvatarUrl,
                            content = content,
                            type = com.example.e_messengerapplication.domain.MessageType.TEXT,
                            time = time,
                            conversationId = conversationId
                        )
                        _messageFlow.tryEmit(message)
                    }
                } catch (e: JSONException) {
                    Log.e("WebSocket", "Parse error", e)
                }
            }, { throwable ->
                Log.e("WebSocket", "Receive error", throwable)
            })?.let { compositeDisposable.add(it) }
    }

    private fun onTopicConversation() {
        stompClient?.topic("/user/conversations")
            ?.subscribe({ msg ->
                Log.d("WebSocket", "Conversation event: ${msg.payload}")
            }, { throwable ->
                Log.e("WebSocket", "Error receiving conversation", throwable)
            })?.let { compositeDisposable.add(it) }
    }
}
