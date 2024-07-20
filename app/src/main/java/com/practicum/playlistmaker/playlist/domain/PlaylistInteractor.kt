package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getLists(): Flow<List<Playlist>>

    suspend fun getList(playlistId: Int): Flow<Playlist>
}