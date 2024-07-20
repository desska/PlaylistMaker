package com.practicum.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.entity.PlaylistErrorType
import com.practicum.playlistmaker.playlist.domain.entity.PlaylistState
import com.practicum.playlistmaker.utils.SingleEventLiveData
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    private val state =
        MutableLiveData<PlaylistState>()

    private val playlistClickEvent = SingleEventLiveData<Playlist>()
    fun observePlaylistClickEvent(): LiveData<Playlist> = playlistClickEvent

    fun observeState(): LiveData<PlaylistState> = state

    private val newListClickEvent = SingleEventLiveData<Boolean>()
    fun getNewListClickEvent(): LiveData<Boolean> = newListClickEvent

    fun onCreateListClick() {
        newListClickEvent.postValue(true)
    }

    fun onPlaylistClick(playlist: Playlist) {
        playlistClickEvent.value = playlist
    }

    fun fillData() {
        viewModelScope.launch {
            interactor.getLists()
                .collect {
                    process(it)
                }
        }
    }

    private fun process(lists: List<Playlist>) {
        if (lists.isNotEmpty()) {
            state.postValue(PlaylistState.Content(lists))
        } else {
            state.postValue(PlaylistState.Error(PlaylistErrorType.EMPTY_PLAYLIST))
        }
    }

}