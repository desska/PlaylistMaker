package com.practicum.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorite")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    var trackId: Int = 0,
    var trackName: String = "",
    var artistName: String = "",
    var trackTime: String = "",
    var artworkUrl100: String = "",
    var trackTimeMillis: Int = 0,
    var collectionName: String = "",
    var releaseDate: Date = Date(),
    var primaryGenreName: String = "",
    var country: String = "",
    var previewUrl: String = "",
    var addDate: Date = Date()
)
