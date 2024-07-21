package com.practicum.playlistmaker.edittracks.domain.entity

import com.practicum.playlistmaker.player.domain.entity.Track

sealed interface TracksState {
    object Empty : TracksState
    data class Content(val data: List<Track>) : TracksState
}