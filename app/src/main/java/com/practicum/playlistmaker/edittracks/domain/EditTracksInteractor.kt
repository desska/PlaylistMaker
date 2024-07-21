package com.practicum.playlistmaker.edittracks.domain

import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface EditTracksInteractor {
    suspend fun getTracks(playlistId: Int): Flow<List<Track>>

    suspend fun deleteTrack(playlistId: Int, trackId: Int)

    suspend fun deleteList(playlistId: Int)

    fun share(string: String)

}