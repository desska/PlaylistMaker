package com.practicum.playlistmaker.playlist.domain.entity

import com.practicum.playlistmaker.newlist.domain.entity.Playlist

sealed interface PlaylistState {
    data class Error(val type: PlaylistErrorType): PlaylistState
    data class Content(val data: List<Playlist>): PlaylistState
}