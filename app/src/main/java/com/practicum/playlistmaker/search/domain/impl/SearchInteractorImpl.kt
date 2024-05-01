package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.domain.SearchServiceRepository
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.entity.Consumer
import com.practicum.playlistmaker.search.domain.entity.Resource
import java.util.concurrent.Executors

class SearchInteractorImpl(private val searchServiceRepository: SearchServiceRepository) :
    SearchInteractor {

    override fun search(song: String, consumer: Consumer<List<Track>>) {

        val executor = Executors.newCachedThreadPool()

        executor.execute {

            val result = searchServiceRepository.search(song)

            if (result.resultCode == 200) {

                consumer.consume(Resource.Data(result.results))

            } else {

                consumer.consume(Resource.Error("Error ${result.resultCode}"))


            }

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
