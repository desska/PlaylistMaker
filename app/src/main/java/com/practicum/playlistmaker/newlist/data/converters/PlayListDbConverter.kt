package com.practicum.playlistmaker.newlist.data.converters

import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.newlist.domain.entity.Playlist

class PlayListDbConverter {
    fun map(playlist: Playlist, cover: String): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            cover = cover
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            cover = playlist.cover,
            quantity = playlist.quantity
        )
    }

}