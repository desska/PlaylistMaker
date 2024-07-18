package com.practicum.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId: Int = 0,
    val trackName: String = "",
    val artistName: String = "",
    val trackTime: String = "",
    val artworkUrl100: String = "",
    val trackTimeMillis: Int = 0,
    val collectionName: String = "",
    val releaseDate: Date = Date(),
    val primaryGenreName: String = "",
    val country: String = "",
    val previewUrl: String = "",
    val addDate: Date = Date()
)
