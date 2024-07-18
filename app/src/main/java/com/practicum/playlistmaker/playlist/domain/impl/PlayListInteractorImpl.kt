package com.practicum.playlistmaker.playlist.domain.impl

import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.playlist.domain.PlayListRepository
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val repository: PlayListRepository) : PlaylistInteractor {
    override suspend fun getLists(): Flow<List<Playlist>> {
        return repository.getLists()
    }
}