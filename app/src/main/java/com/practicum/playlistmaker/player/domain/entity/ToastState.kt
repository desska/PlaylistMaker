package com.practicum.playlistmaker.player.domain.entity

sealed interface ToastState {
    data class IsAdded(val name: String): ToastState
    data class IsInList(val name: String): ToastState
}