package com.example.callsimulationapp

import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.example.callsimulationapp.databinding.ActivityFakeCallBinding

class FakeCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFakeCallBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFakeCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.answerCallButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.setVolume(0f, 0f)
            }
            // Hide the answer button and show the Chronometer
            binding.answerCallButton.visibility = View.GONE
            binding.declineCallButton.visibility = View.GONE
            binding.callDuration.visibility = View.VISIBLE

            // Start the Chronometer
            binding.callDuration.base = SystemClock.elapsedRealtime()
            binding.callDuration.start()
        }


        binding.declineCallButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.setVolume(0f, 0f)
            }
            finishAndRemoveTask()
        }

        binding.endCallButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.setVolume(0f, 0f)
            }
            finishAndRemoveTask()
        }

        // Play the ringtone
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        mediaPlayer = MediaPlayer.create(this, uri)
        mediaPlayer.isLooping = true
        mediaPlayer.setVolume(50f, 50f)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

}
