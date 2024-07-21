package com.practicum.playlistmaker.newlist.domain.entity

sealed interface NewListState {
    object ReadyToSave: NewListState
    object NotReadyToSave: NewListState
    data class Created(val name: String): NewListState
    object Confirm: NewListState
    object Exit: NewListState
}