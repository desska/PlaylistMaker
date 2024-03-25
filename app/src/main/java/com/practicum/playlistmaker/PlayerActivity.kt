package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var player = MediaPlayer()

    private lateinit var track: Track

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(PLAYER_TRACKS_KEY, Track::class.java)!!
        else
            intent.getSerializableExtra(PLAYER_TRACKS_KEY) as Track

        handler = Handler(Looper.getMainLooper())

        binding.playButton.isEnabled = false
        binding.playButton.setImageResource(R.drawable.play_button)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.player_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        updateFromTrack(track)
        preparePlayer()

        binding.playButton.setOnClickListener { playbackControl() }

    }

    override fun onDestroy() {
        super.onDestroy()

        if (playerState != STATE_URL_ERROR) {

            handler?.removeCallbacks(updater)
            player.release()

        }


    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    companion object {

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val STATE_URL_ERROR = 4
        private const val REFRESH_TIME_MS = 300L

    }

    private var playerState = STATE_DEFAULT

    private fun preparePlayer() {

        if ((track.previewUrl == "") || (track.previewUrl == null)) {

            playerState = STATE_URL_ERROR
            return
        }

        player.setDataSource(track.previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {

            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED

        }

        player.setOnCompletionListener {

            binding.playButton.setImageResource(R.drawable.play_button)
            playerState = STATE_PREPARED
            handler?.removeCallbacks(updater)
            binding.playerElapsedTime.text = getString(R.string.track_null_time)

        }

    }

    private val updater = object : Runnable {

        override fun run() {

            val pos = player.currentPosition
            binding.playerElapsedTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(pos)
            handler?.postDelayed(this, REFRESH_TIME_MS)

        }

    }

    private fun startPlayer() {

        if (playerState == STATE_URL_ERROR) {

            return

        }

        player.start()
        binding.playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING

        handler?.postDelayed(

            updater,
            REFRESH_TIME_MS

        )

    }

    private fun pausePlayer() {

        if (playerState == STATE_URL_ERROR) {

            return

        }

        player.pause()
        binding.playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
        handler?.removeCallbacks(updater)


    }

    private fun playbackControl() {

        when (playerState) {

            STATE_PLAYING -> {

                pausePlayer()

            }

            STATE_PAUSED, STATE_PREPARED -> {

                startPlayer()

            }

        }


    }

    private fun updateFromTrack(track: Track) {

        binding.playerTrackName.text = track.trackName
        binding.playerArtistName.text = track.artistName
        binding.timeInfo.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
                .trimStart('0')
        binding.collectionInfo.text = track.collectionName
        binding.yearInfo.text = track.getReleaseYear()
        binding.genreInfo.text = track.primaryGenreName
        binding.countryInfo.text = track.country
        binding.playerElapsedTime.text = getString(R.string.track_sample_time)

        Glide.with(binding.playerCover)
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform(
                RoundedCorners(
                    Utils.dpToPx(
                        binding.playerCover.context.resources.getInteger(R.integer.track_big_cover_corner)
                            .toFloat(), binding.playerCover.context
                    )
                )
            )
            .placeholder(R.drawable.big_cover_placeholder)
            .into(binding.playerCover)

    }

}