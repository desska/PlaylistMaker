package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.player.domain.entity.Track


object TrackMapper {

    fun map(track: Track): TrackInfo {

        return TrackInfo(

         trackId = track.trackId ?: -1,
         trackName= track.trackName ?: "",
         artistName= track.artistName ?: "",
         trackTime= formatTime(track.trackTimeMillis),
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