package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.player.domain.entity.Track

interface HistoryLocalStorage {

    fun add(track: Track)

    fun clear()

    fun get(): MutableList<Track>

}