package com.practicum.playlistmaker.domain.entity

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Track(
    val trackId: Int?,
    val trackName: String?,
    val artistName: String?,
    val trackTime: String?,
    val artworkUrl100: String?,
    val trackTimeMillis: Int?,
    val collectionName: String?,
    val releaseDate: Date?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?

): Serializable {

    companion object
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")?: ""

    fun getReleaseYear(): String {

        val formatter = SimpleDateFormat("yyyy", Locale.ENGLISH)
        val formatDate =
            if (releaseDate == null) {
                ""
            } else {
                formatter.format(releaseDate)
            }

        return formatDate

    }

}
