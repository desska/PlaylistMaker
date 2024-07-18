package com.practicum.playlistmaker.newlist.domain.entity

data class Playlist(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val cover: String = "",
    val quantity: Int = 0
)
