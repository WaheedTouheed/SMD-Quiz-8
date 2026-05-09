package com.example.androidtopics

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MediaActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val playAudioButton: Button = findViewById(R.id.playAudioButton)
        val playVideoButton: Button = findViewById(R.id.playVideoButton)

        playAudioButton.setOnClickListener {
            playAudioFromRaw()
        }

        playVideoButton.setOnClickListener {
            val videoUrl = "https://youtu.be/FKojb_16X5Q?si=ga5b6ytulTbKPX-M"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            startActivity(intent)
        }
    }

    private fun playAudioFromRaw() {
        // Stop and release previous instance if any
        stopAudio()

        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound1)
            mediaPlayer?.setOnCompletionListener {
                stopAudio()
            }
            mediaPlayer?.start()
            Toast.makeText(this, "Playing Audio", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error playing audio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopAudio() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }

    override fun onStop() {
        super.onStop()
        stopAudio()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
    }
}