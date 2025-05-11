package com.example.e_messengerapplication.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

object Constant {
    const val BASE_URL = "http://192.168.2.9:8080/e-messenger/"
    const val WEBSOCKET_URL = "ws://192.168.2.9:8080/e-messenger/ws/websocket"
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatMessageTime(raw: String): String {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart().appendPattern(".SSSSSSS").optionalEnd()
            .optionalStart().appendPattern(".SSSSSS").optionalEnd()
            .optionalStart().appendPattern(".SSS").optionalEnd()
            .optionalStart().appendPattern(".SS").optionalEnd()
            .optionalStart().appendPattern(".S").optionalEnd()
            .toFormatter()

        val dateTime = LocalDateTime.parse(raw, formatter)

        val today = LocalDate.now()
        val messageDate = dateTime.toLocalDate()
        val timePart = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        return if (messageDate == today) {
            timePart
        } else {
            val dayOfWeek = when (dateTime.dayOfWeek) {
                DayOfWeek.MONDAY -> "T.2"
                DayOfWeek.TUESDAY -> "T.3"
                DayOfWeek.WEDNESDAY -> "T.4"
                DayOfWeek.THURSDAY -> "T.5"
                DayOfWeek.FRIDAY -> "T.6"
                DayOfWeek.SATURDAY -> "T.7"
                DayOfWeek.SUNDAY -> "CN"
            }
            "$dayOfWeek $timePart"
        }
    }

}