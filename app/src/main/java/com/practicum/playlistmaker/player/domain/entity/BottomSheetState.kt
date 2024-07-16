package com.practicum.playlistmaker.player.domain.entity

sealed interface BottomSheetState {
    object HIDDEN: BottomSheetState
    object COLLAPSED: BottomSheetState
}