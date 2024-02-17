package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var coverView: ImageView
    private lateinit var trackNameView: TextView
    private lateinit var artistNameView: TextView
    private lateinit var timeInfoView: TextView
    private lateinit var collectionInfoView: TextView
    private lateinit var yearInfoView: TextView
    private lateinit var genreInfoView: TextView
    private lateinit var countryInfoView: TextView
    private lateinit var elapsedTimeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        coverView = findViewById(R.id.player_cover)
        trackNameView = findViewById(R.id.player_track_name)
        artistNameView = findViewById(R.id.player_artist_name)
        timeInfoView = findViewById(R.id.time_info)
        collectionInfoView = findViewById(R.id.collection_info)
        yearInfoView = findViewById(R.id.year_info)
        genreInfoView = findViewById(R.id.genre_info)
        countryInfoView = findViewById(R.id.country_info)
        elapsedTimeView = findViewById(R.id.player_elapsed_time)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.player_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val trackAsJson = intent.getStringExtra(PLAYER_TRACKS_KEY)
        val track = Gson().fromJson(trackAsJson, Track::class.java)

        updateFromTrack(track)

    }

    private fun updateFromTrack(track: Track) {

        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        timeInfoView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis).trimStart('0')
        collectionInfoView.text = track.collectionName
        yearInfoView.text = track.getReleaseYear()
        genreInfoView.text = track.primaryGenreName
        countryInfoView.text = track.country
        elapsedTimeView.text = "0:30"

        Glide.with(coverView)
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform(
                RoundedCorners(
                    Utils.dpToPx(
                        coverView.context.resources.getInteger(R.integer.track_big_cover_corner)
                            .toFloat(), coverView.context
                    )
                )
            )
            .placeholder(R.drawable.big_cover_placeholder)
            .into(coverView)

    }

}