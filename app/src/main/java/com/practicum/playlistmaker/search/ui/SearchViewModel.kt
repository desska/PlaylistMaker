package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.utils.SingleEventLiveData
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.entity.Consumer
import com.practicum.playlistmaker.search.domain.entity.ErrorType
import com.practicum.playlistmaker.search.domain.entity.Resource
import com.practicum.playlistmaker.search.domain.entity.SearchState

class SearchViewModel(private val interactor: SearchInteractor) : ViewModel() {

    private val searchState = MutableLiveData<SearchState>()
    fun getSearchState(): LiveData<SearchState> = searchState

    private val historyState = MutableLiveData(interactor.getHistory() as MutableList<Track>)
    fun getHistoryState(): LiveData<MutableList<Track>> = historyState

    private val trackClickEvent = SingleEventLiveData<Track>()
    fun getTrackClickEvent(): LiveData<Track> = trackClickEvent

    private val latestSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(HANDLER_TOKEN)
    }

    fun onEditorActionDone(text: String) {

        loadTracks(text)

    }

    fun onTrackClick(track: Track) {

        trackClickEvent.value = track

    }

    fun onTextChanged(text: String) {

        if (text == latestSearchText) return
        handler.removeCallbacksAndMessages(HANDLER_TOKEN)

        val searchRun = Runnable { loadTracks(text) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRun,
            HANDLER_TOKEN,
            postTime,
        )

    }

    fun onTrackListClear() {

        searchState.value = SearchState.Content(emptyList())

    }

    fun onHistoryClear() {

        interactor.clearHistory()
        historyState.value = mutableListOf()

    }

    fun addToHistory(track: Track) {

        interactor.addToHistory(track)
        updateHistory()
    }

    private  fun updateHistory(){

        historyState.value = interactor.getHistory() as MutableList<Track>

    }

    private fun loadTracks(song: String) {

        if (song.isEmpty()) return

        searchState.value = SearchState.Loading

        interactor.search(song = song, consumer = object : Consumer<List<Track>> {
            override fun consume(data: Resource<List<Track>>) {

                when (data) {

                    is Resource.Data -> {


                        if (data.value.isNotEmpty()) {
                            searchState.postValue(SearchState.Content(data.value))
                        } else {

                            searchState.postValue(SearchState.Error(ErrorType.EMPTY))
                        }

                    }

                    is Resource.Error -> {

                        searchState.postValue(SearchState.Error(ErrorType.EMPTY))

                    }

                }


            }


        })

    }

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val HANDLER_TOKEN = Any()

        fun getViewModelFactory(interactor: SearchInteractor): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        interactor
                    ) as T

                }

            }
    }

}