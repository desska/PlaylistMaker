package com.practicum.playlistmaker.ui.player

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.PLAYER_TRACKS_KEY
import com.practicum.playlistmaker.PlayerState
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.Utils
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.presentation.mapper.TrackMapper
import com.practicum.playlistmaker.presentation.model.TrackInfo
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private val playerInteractor = Creator.providePlayerInteractor()

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var track: Track
    private lateinit var trackInfo: TrackInfo
    private var handler: Handler? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(PLAYER_TRACKS_KEY, Track::class.java)!!
        else
            intent.getSerializableExtra(PLAYER_TRACKS_KEY) as Track

        trackInfo = TrackMapper.map(track)

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

        updateFromTrack(trackInfo)
        preparePlayer()

        binding.playButton.setOnClickListener { playbackControl() }

    }

    override fun onDestroy() {

        super.onDestroy()

        if (playerInteractor.getState() != PlayerState.STATE_URL_ERROR) {

            handler?.removeCallbacks(updater)


        }

        playerInteractor.release()

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    companion object {

        private const val REFRESH_TIME_MS = 300L

    }

    private fun preparePlayer() {

        playerInteractor.prepare(trackInfo.previewUrl, object : PlayerInteractor.OnPreparedListener {

            override fun onPrepared() {

                binding.playButton.isEnabled = true

            }

            override fun onComplete() {

                binding.playButton.setImageResource(R.drawable.play_button)
                handler?.removeCallbacks(updater)
                binding.playerElapsedTime.text = getString(R.string.track_null_time)

            }

        })

    }

    private val updater = object : Runnable {

        override fun run() {

            val pos = playerInteractor.getCurrentPosition()
            binding.playerElapsedTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(pos)
            handler?.postDelayed(this, REFRESH_TIME_MS)

        }

    }

    private fun startPlayer() {

        if (playerInteractor.getState() == PlayerState.STATE_URL_ERROR) {

            return

        }

        playerInteractor.start()
        binding.playButton.setImageResource(R.drawable.pause_button)

        handler?.postDelayed(

            updater,
            REFRESH_TIME_MS

        )

    }

    private fun pausePlayer() {

        if (playerInteractor.getState() == PlayerState.STATE_URL_ERROR) {

            return

        }

        playerInteractor.pause()
        binding.playButton.setImageResource(R.drawable.play_button)
        handler?.removeCallbacks(updater)


    }

    private fun playbackControl() {

        when (playerInteractor.getState()) {

            PlayerState.STATE_PLAYING -> {

                pausePlayer()

            }

            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> {

                startPlayer()

            }

            else -> {}
        }


    }

    private fun updateFromTrack(track: TrackInfo) {

        binding.playerTrackName.text = track.trackName
        binding.playerArtistName.text = track.artistName
        binding.timeInfo.text = track.trackTime
        binding.collectionInfo.text = track.collectionName
        binding.yearInfo.text = track.releaseYear
        binding.genreInfo.text = track.primaryGenreName
        binding.countryInfo.text = track.country
        binding.playerElapsedTime.text = getString(R.string.track_sample_time)

        Glide.with(binding.playerCover)
            .load(track.coverArtwork)
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