package com.practicum.playlistmaker.player.domain.entity

sealed interface PlayerState {

    object Default : PlayerState
    object Playing : PlayerState
    object Prepared : PlayerState
    object Paused : PlayerState
    data class Error(val msg: String) :PlayerState

}