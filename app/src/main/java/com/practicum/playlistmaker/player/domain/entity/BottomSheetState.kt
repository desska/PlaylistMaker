package com.practicum.playlistmaker.player.domain.entity

sealed interface BottomSheetState {
    object Hidden: BottomSheetState
    object Collapsed: BottomSheetState
}