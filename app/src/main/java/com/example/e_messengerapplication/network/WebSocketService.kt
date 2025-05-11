package com.example.e_messengerapplication.data.websocket

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.e_messengerapplication.TokenManager
import com.example.e_messengerapplication.domain.Message
import com.example.e_messengerapplication.utils.Constant
import com.example.e_messengerapplication.utils.Constant.WEBSOCKET_URL
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.json.JSONException
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject

class WebSocketService @Inject constructor(
    private val tokenManager: TokenManager
) {
    private var stompClient: StompClient? = null
    private val compositeDisposable = CompositeDisposable()

    private val _messageFlow = MutableSharedFlow<Message>(replay = 1) // gửi ra ngoài
    val messageFlow: SharedFlow<Message> = _messageFlow

    @RequiresApi(Build.VERSION_CODES.O)
    fun connect(
        conversationId: String,
    ) {
        val headers = mapOf("Authorization" to "Bearer ${tokenManager.getAccessToken()}")
        stompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            WEBSOCKET_URL,
            headers
        ).apply {
            withClientHeartbeat(10000)
            withServerHeartbeat(10000)
        }

        stompClient?.connect()

        // Lắng nghe trạng thái kết nối
        stompClient?.lifecycle()?.subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED -> {
                    val topic = "/user/queue/messages"
                    stompClient?.topic(topic)
                        ?.subscribe({ msg ->
                            try {
                                Log.d("WebSocketService", "Raw payload: ${msg.payload}")
                                val json = JSONObject(msg.payload)
                                val senderName = json.getString("senderName")
                                val content = json.getString("content")
                                val sentAt = Constant.formatMessageTime(json.getString("sentAt"))
                                val senderId = json.getString("senderId")
                                val message = Message(content, senderId, sentAt)
                                _messageFlow.tryEmit(message)

                            } catch (e: JSONException) {
                                Log.e("WebSocket", "Parse error", e)
                            }
                        }, { throwable ->
                            Log.e("WebSocket", "Error receiving message", throwable)
                        })?.let { compositeDisposable.add(it) }
                }

                LifecycleEvent.Type.ERROR -> Log.e("WebSocket", "Connection error", event.exception)
                LifecycleEvent.Type.CLOSED -> Log.d("WebSocket", "Connection closed")
                else -> {}
            }
        }?.let { compositeDisposable.add(it) }
    }

    @SuppressLint("CheckResult")
    fun sendMessage(conversationId: String, message: String) {
        val destination = "/chat/$conversationId/send-message"
        val payload = JSONObject()
            .put("content", message)
            .put("type", "TEXT")
            .toString()
        stompClient?.send(destination, payload)?.subscribe({}, {
            Log.e("WebSocket", "Send error", it)
        })
    }

    fun disconnect() {
        stompClient?.disconnect()
        compositeDisposable.clear()
    }
}
