package com.practicum.playlistmaker.media.domain.entity

sealed interface FavoriteState {
    data class Error(val type: FavoriteErrorType) : FavoriteState
}