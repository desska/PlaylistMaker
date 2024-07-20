package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    fun getLists(): Flow<List<Playlist>>

    fun getList(playlistId: Int): Flow<Playlist>
}