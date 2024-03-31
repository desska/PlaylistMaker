package com.practicum.playlistmaker.presentation.mapper

import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.presentation.model.TrackInfo


object TrackMapper {

    fun map(track: Track): TrackInfo {

        return TrackInfo(

         trackId = track.trackId ?: -1,
         trackName= track.trackName ?: "",
         artistName= track.artistName ?: "",
         trackTime= formatTime(track.trackTimeMillis) ?: "",
         collectionName= track.collectionName ?: "",
         releaseYear = track.getReleaseYear(),
         primaryGenreName= track.primaryGenreName ?: "",
         country= track.country ?: "",
         previewUrl = track.previewUrl ?: "",
         coverArtwork = track.getCoverArtwork()

        )

    }

    private fun formatTime(trackTimeMillis: Int?): String {

        return TrackTimeFormatter.formatTime(trackTimeMillis)

    }

}