package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.SearchServiceRepository
import com.practicum.playlistmaker.search.domain.entity.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchInteractorImpl(private val searchServiceRepository: SearchServiceRepository) :
    SearchInteractor {

    override fun search(song: String): Flow<Resource<List<Track>>> = flow {
        val result = searchServiceRepository.search(song)

        if (result.resultCode == 200) {
            emit(Resource.Data(result.results))
        } else {
            emit(Resource.Error("Error ${result.resultCode}"))
        }
    }

    override fun addToHistory(track: Track) {
        searchServiceRepository.addToHistory(track)

    }

    override fun getHistory(): List<Track> {
        return searchServiceRepository.getHistory()

    }

    override fun clearHistory() {
        searchServiceRepository.clearHistory()

    }
}
