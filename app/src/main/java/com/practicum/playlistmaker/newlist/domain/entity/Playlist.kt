package com.practicum.playlistmaker.newlist.domain.entity

import java.io.Serializable

data class Playlist(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val cover: String = "",
    val quantity: Int = 0
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true

        if (other !is Playlist) {
            return false;
        }
        val playlist = other as Playlist

        return (playlist.name == this.name) and (playlist.description == this.description) and (playlist.cover == this.cover) and (playlist.id == this.id)

    }
}
