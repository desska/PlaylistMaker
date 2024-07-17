package com.practicum.playlistmaker.data.db

import androidx.room.Entity

@Entity(tableName = "track_playlist", primaryKeys = ["playlistId", "trackId"])
data class TrackPlaylistEntity(
    val playlistId: Int = 0,
    val trackId: Int = 0,
)
