package com.practicum.playlistmaker.media.domain.entity

sealed interface PlaylistState {
    data class Error(val type: PlaylistErrorType): PlaylistState
}