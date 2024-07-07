package com.practicum.playlistmaker.player.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.search.ui.PLAYER_TRACKS_KEY
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.utils.Utils
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.entity.PlayerState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private lateinit var trackInfo: TrackInfo
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(PLAYER_TRACKS_KEY, Track::class.java)!!
        else
            intent.getSerializableExtra(PLAYER_TRACKS_KEY) as Track

        trackInfo = TrackMapper.map(track)

        setSupportActionBar(binding.playerToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        binding.playerToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        updateFromTrack(trackInfo)

        binding.playButton.setOnClickListener { viewModel.onPlaybackControl() }

        binding.playerFavoriteButton.setOnClickListener { viewModel.toggleFavorite() }

        viewModel.getPlayerState().observe(this) {
            when (it) {
                is PlayerState.Playing -> {
                    binding.playButton.setImageResource(R.drawable.pause_button)

                }

                is PlayerState.Prepared -> {
                    binding.playButton.setImageResource(R.drawable.play_button)
                    binding.playerElapsedTime.text = getString(R.string.track_null_time)

                }

                is PlayerState.Paused -> {
                    binding.playButton.setImageResource(R.drawable.play_button)
                    binding.playButton.isEnabled = true

                }

                is PlayerState.Error -> {
                    binding.playButton.isEnabled = false

                }

                is PlayerState.Default -> {
                    binding.playButton.isEnabled = false
                    binding.playButton.setImageResource(R.drawable.play_button)
                }

                else -> {}
            }
        }

        viewModel.getProgress().observe(this) {
            binding.playerElapsedTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
        }

        viewModel.getIsFavoriteState().observe(this) {
            updateFavoriteButton(it)
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
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

    private fun updateFavoriteButton(isFavorite: Boolean) {
        val res = if (isFavorite) {
            R.drawable.favorite_on
        } else {
            R.drawable.favorite
        }
        binding.playerFavoriteButton.setImageResource(res)
    }

}