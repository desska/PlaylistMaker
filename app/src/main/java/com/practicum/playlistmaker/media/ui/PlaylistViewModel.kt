package com.practicum.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.domain.entity.FavoriteState
import com.practicum.playlistmaker.media.domain.entity.PlaylistErrorType
import com.practicum.playlistmaker.media.domain.entity.PlaylistState

class PlaylistViewModel: ViewModel() {

    private val state = MutableLiveData<PlaylistState>(PlaylistState.Error(PlaylistErrorType.EMPTY_PLAYLIST))
    fun observeState(): LiveData<PlaylistState> = state


}