package com.example.e_messengerapplication.ui.chat

import com.example.e_messengerapplication.utils.Constant
import com.masoudss.lib.WaveformSeekBar
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.e_messengerapplication.R

object AudioMessageHelper {

    fun setupAudio(
        context: Context,
        audioUrl: String,
        waveformSeekBar: WaveformSeekBar,
        playPauseButton: Button,
        durationText: TextView
    ) {
        var isPlaying = false
        durationText.text = "00:00"
        waveformSeekBar.progress = 0F

        Constant.downloadAudioToFile(context, audioUrl) { localFile ->
            if (localFile != null) {
                waveformSeekBar.setSampleFrom(localFile)

                val mediaPlayer = MediaPlayer().apply {
                    setDataSource(localFile.absolutePath)
                    prepareAsync()
                    setOnPreparedListener {
                        val durationMs = it.duration
                        durationText.text = formatTime(durationMs)
                    }

                    setOnCompletionListener {
                        waveformSeekBar.progress = 0F
                        isPlaying = false
                        playPauseButton.setBackgroundResource(R.drawable.ic_play)
                    }
                }

                playPauseButton.setOnClickListener {
                    if (isPlaying) {
                        mediaPlayer.pause()
                        playPauseButton.setBackgroundResource(R.drawable.ic_play)
                    } else {
                        mediaPlayer.start()
                        playPauseButton.setBackgroundResource(R.drawable.ic_pause)
                        val handler = Handler(Looper.getMainLooper())
                        handler.post(object : Runnable {
                            override fun run() {
                                if (isPlaying && mediaPlayer.isPlaying) {
                                    val current = mediaPlayer.currentPosition
                                    val total = mediaPlayer.duration
                                    val progress = (current.toFloat() / total) * 100
                                    waveformSeekBar.progress = progress
                                    handler.postDelayed(this, 100)
                                }
                            }
                        })
                    }
                    isPlaying = !isPlaying
                }
            } else {
                Log.e("AudioMessageHelper", "Failed to download audio file.")
            }
        }
    }

    private fun formatTime(ms: Int): String {
        val seconds = ms / 1000
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs)
    }
}
