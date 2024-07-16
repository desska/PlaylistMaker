package com.practicum.playlistmaker.newlist.domain.entity

sealed interface NewListState {
    object ReadyToCreate: NewListState
    object NotReadyToCreate: NewListState
    data class Created(val name: String): NewListState
    object Confirm: NewListState
    object Exit: NewListState
}