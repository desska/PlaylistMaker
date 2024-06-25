package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.domain.entity.Resource
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun search(song: String): Flow<Resource<List<Track>>>

    fun addToHistory(track: Track)

    fun getHistory(): List<Track>

    fun clearHistory()

}