package com.practicum.playlistmaker.data.db

import androidx.room.Entity

@Entity(tableName = "track_playlist", primaryKeys = ["playlistId", "trackId"])
data class TrackPlaylistEntity(
    var playlistId: Int = 0,
    var trackId: Int = 0,
)
