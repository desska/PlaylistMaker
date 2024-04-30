package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.domain.entity.Consumer

interface SearchInteractor {

    fun search(song: String, consumer: Consumer<List<Track>>)

    fun addToHistory(track: Track)

    fun getHistory(): List<Track>

    fun clearHistory()

}