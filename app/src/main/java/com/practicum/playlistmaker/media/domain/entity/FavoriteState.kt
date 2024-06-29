package com.practicum.playlistmaker.media.domain.entity

import com.practicum.playlistmaker.player.domain.entity.Track

sealed interface FavoriteState {
    data class Error(val type: FavoriteErrorType) : FavoriteState
    class Content(val data: List<Track>): FavoriteState
}