package com.practicum.playlistmaker.search.domain.entity

interface Consumer<T> {

    fun consume(data: Resource<T>)

}