package com.practicum.playlistmaker.search.domain.entity

sealed interface Resource<T> {

    data class Data<T>(val value: T): Resource<T>
    data class Error<T>(val msg: String): Resource<T>
}