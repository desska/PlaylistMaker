package com.practicum.playlistmaker.favorite.data.converters

import com.practicum.playlistmaker.data.db.FavoriteEntity
import com.practicum.playlistmaker.player.domain.entity.Track
import java.util.Date

class FavoriteDbConverter {

    fun map(track: Track, addDate: Date): FavoriteEntity {
        return FavoriteEntity(
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
            addDate = addDate,
            trackTimeMillis = track.trackTimeMillis ?: 0
        )
    }

    fun map(track: FavoriteEntity): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime, artworkUrl100 = track.artworkUrl100,
            trackTimeMillis = track.trackTimeMillis,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
    }

}