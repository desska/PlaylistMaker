package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.entity.ErrorType
import com.practicum.playlistmaker.search.domain.entity.Resource
import com.practicum.playlistmaker.search.domain.entity.SearchState
import com.practicum.playlistmaker.utils.SingleEventLiveData
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val interactor: SearchInteractor) : ViewModel() {
    private val searchState = MutableLiveData<SearchState>()
    fun getSearchState(): LiveData<SearchState> = searchState

    private val historyState = MutableLiveData(interactor.getHistory() as MutableList<Track>)
    fun getHistoryState(): LiveData<MutableList<Track>> = historyState

    private val trackClickEvent = SingleEventLiveData<Track>()
    fun getTrackClickEvent(): LiveData<Track> = trackClickEvent

    private var latestSearchText: String? = null

    fun onEditorActionDone(text: String) {
        if (text != latestSearchText) {
            loadTracks(text)
        }
    }

    fun onTrackClick(track: Track) {
        trackClickEvent.value = track
    }

    val onTextChangedDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { text ->
            if (text != latestSearchText) {
                latestSearchText = text
                loadTracks(text)
            }
        }

    fun onTextChanged(text: String) {
        onTextChangedDebounce(text)
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

    private fun updateHistory() {
        historyState.value = interactor.getHistory() as MutableList<Track>

    }

    private fun loadTracks(song: String) {
        if (song.isEmpty()) return

        searchState.value = SearchState.Loading

        viewModelScope.launch {
            interactor.search(song = song).collect { data ->
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
        }

    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}