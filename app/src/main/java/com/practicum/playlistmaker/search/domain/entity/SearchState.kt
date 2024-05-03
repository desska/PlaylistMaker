package com.practicum.playlistmaker.search.domain.entity

import com.practicum.playlistmaker.player.domain.entity.Track

sealed interface SearchState {

    data class Content(val data: List<Track>): SearchState
    object Loading: SearchState
    data class Error(val type: ErrorType): SearchState

}