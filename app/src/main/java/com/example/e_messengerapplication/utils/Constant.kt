package com.example.e_messengerapplication.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.WeekFields
import java.util.Locale

object Constant {
    const val TAG_USER = "USER"
    const val TAG_CONVERSATION = "CONVERSATION"
    const val TAG_MESSAGE = "MESSAGE"

    const val BASE_URL = "http://10.10.30.136:8080/e-messenger/"
    const val WEBSOCKET_URL = "ws://10.10.30.136:8080/e-messenger/ws/websocket"

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatMessageTime(isoTime: String): String {
        // Parse ISO 8601 string to Instant
        val instant = Instant.parse(isoTime)
        val zoneId = ZoneId.systemDefault()
        val dateTime = instant.atZone(zoneId)

        val now = ZonedDateTime.now(zoneId)

        return when {
            dateTime.toLocalDate().isEqual(now.toLocalDate()) -> {
                // Cùng ngày => hh:mm
                dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            }
            isSameWeek(dateTime.toLocalDate(), now.toLocalDate()) -> {
                // Cùng tuần => T.2, T.3,..., CN
                val dayOfWeek = dateTime.dayOfWeek
                when (dayOfWeek) {
                    DayOfWeek.MONDAY -> "T.2"
                    DayOfWeek.TUESDAY -> "T.3"
                    DayOfWeek.WEDNESDAY -> "T.4"
                    DayOfWeek.THURSDAY -> "T.5"
                    DayOfWeek.FRIDAY -> "T.6"
                    DayOfWeek.SATURDAY -> "T.7"
                    DayOfWeek.SUNDAY -> "CN"
                }
            }
            else -> {
                // Khác tuần => dd ThM
                val day = dateTime.dayOfMonth
                val month = dateTime.monthValue
                "$day Th$month"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isSameWeek(date1: LocalDate, date2: LocalDate): Boolean {
        val weekFields = WeekFields.of(Locale.getDefault())
        val week1 = date1.get(weekFields.weekOfWeekBasedYear())
        val week2 = date2.get(weekFields.weekOfWeekBasedYear())
        val year1 = date1.get(weekFields.weekBasedYear())
        val year2 = date2.get(weekFields.weekBasedYear())
        return week1 == week2 && year1 == year2
    }

    fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("resource", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun downloadAudioToFile(context: Context, url: String, onDownloaded: (File?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val secureUrl = url.replace("http://", "https://")
                val connection = URL(secureUrl).openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    withContext(Dispatchers.Main) { onDownloaded(null) }
                    return@launch
                }

                val file = File.createTempFile("temp_audio", ".m4a", context.cacheDir)
                connection.inputStream.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                withContext(Dispatchers.Main) {
                    onDownloaded(file)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onDownloaded(null)
                }
            }
        }
    }

}