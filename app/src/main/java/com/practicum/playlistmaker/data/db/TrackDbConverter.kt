package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.player.domain.entity.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class TrackDbConverter {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId ?: -1,
            trackName = track.trackName ?: "",
            artistName = track.artistName ?: "",
            trackTime = track.trackTime ?: "",
            collectionName = track.collectionName ?: "",
            releaseDate = track.releaseDate ?: Date(),
            primaryGenreName = track.primaryGenreName ?: "",
            country = track.country ?: "",
            previewUrl = track.previewUrl ?: "",
            artworkUrl100 = track.artworkUrl100 ?: "",
            trackTimeMillis = track.trackTimeMillis ?: 0
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
                .trimStart('0'),
            artworkUrl100 = track.artworkUrl100,
            trackTimeMillis = track.trackTimeMillis,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
    }

}