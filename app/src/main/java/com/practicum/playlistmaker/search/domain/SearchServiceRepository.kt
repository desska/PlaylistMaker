package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.data.entity.TrackSearchResponse

interface SearchServiceRepository {

    fun search(song: String): TrackSearchResponse

    fun addToHistory(track: Track)

    fun getHistory(): List<Track>

    fun clearHistory()

}