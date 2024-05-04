package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.data.entity.ItunesService
import com.practicum.playlistmaker.search.domain.SearchServiceRepository
import com.practicum.playlistmaker.search.data.entity.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.HistoryLocalStorage
import java.lang.Exception

class SearchServiceRepositoryImpl(
    private val historyLocalStorage: HistoryLocalStorage,
    private val itunesService: ItunesService
) :
    SearchServiceRepository {

    override fun search(song: String): TrackSearchResponse {

        return try {
            val response = itunesService.search(song).execute()
            val networkResponse = response.body() ?: TrackSearchResponse(0, mutableListOf())
            networkResponse.apply { resultCode = response.code() }
        } catch (ex: Exception) {

            TrackSearchResponse(0, mutableListOf()).apply { resultCode = 400 }

        }

    }

    override fun addToHistory(track: Track) {

        historyLocalStorage.add(track)

    }

    override fun getHistory(): List<Track> {

        return historyLocalStorage.get()

    }

    override fun clearHistory() {

        historyLocalStorage.clear()

    }
}