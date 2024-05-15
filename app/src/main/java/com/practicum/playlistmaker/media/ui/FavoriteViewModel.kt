package com.practicum.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.domain.entity.FavoriteErrorType
import com.practicum.playlistmaker.media.domain.entity.FavoriteState

class FavoriteViewModel: ViewModel() {

    private val state = MutableLiveData<FavoriteState>(FavoriteState.Error(FavoriteErrorType.EMPTY_MEDIA))
    fun observeState(): LiveData<FavoriteState> = state


}