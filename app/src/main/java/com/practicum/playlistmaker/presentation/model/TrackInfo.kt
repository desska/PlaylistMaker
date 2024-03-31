package com.practicum.playlistmaker.presentation.model

data class TrackInfo(

    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val collectionName: String,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val coverArtwork: String

)
