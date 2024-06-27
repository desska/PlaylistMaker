package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.data.entity.ItunesService
import com.practicum.playlistmaker.search.data.entity.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.HistoryLocalStorage
import com.practicum.playlistmaker.search.domain.SearchServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchServiceRepositoryImpl(
    private val historyLocalStorage: HistoryLocalStorage,
    private val itunesService: ItunesService
) :
    SearchServiceRepository {

    override suspend fun search(song: String): TrackSearchResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = itunesService.search(song)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                TrackSearchResponse(0, mutableListOf()).apply { resultCode = 400 }

            }
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