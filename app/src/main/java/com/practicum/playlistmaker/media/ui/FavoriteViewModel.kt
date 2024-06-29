package com.practicum.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.FavoriteInteractor
import com.practicum.playlistmaker.media.domain.entity.FavoriteErrorType
import com.practicum.playlistmaker.media.domain.entity.FavoriteState
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.utils.SingleEventLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    private val trackClickEvent = SingleEventLiveData<Track>()
    fun getTrackClickEvent(): LiveData<Track> = trackClickEvent

    private val favoriteState =
        MutableLiveData<FavoriteState>()

    fun observeState(): LiveData<FavoriteState> = favoriteState

    fun onTrackClick(track: Track) {
        trackClickEvent.value = track
    }

    private fun renderState(state: FavoriteState) {
        favoriteState.postValue(state)
    }

    fun fillData() {
        viewModelScope.launch {
            favoriteInteractor.getFavorite()
                .collect {
                    process(it)
                }
        }

    }

    private fun process(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            renderState(FavoriteState.Content(tracks))
        } else {
            renderState(FavoriteState.Error(FavoriteErrorType.EMPTY_MEDIA))
        }
    }
}