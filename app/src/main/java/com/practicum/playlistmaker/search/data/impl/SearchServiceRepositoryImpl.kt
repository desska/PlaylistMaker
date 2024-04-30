package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.data.entity.ItunesService
import com.practicum.playlistmaker.search.data.SearchServiceRepository
import com.practicum.playlistmaker.search.data.entity.TrackSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class SearchServiceRepositoryImpl(val historyLocalStorage: HistoryLocalStorage) :
    SearchServiceRepository {

    private val itunesUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesService::class.java)

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