package com.practicum.playlistmaker.edittracks.domain.impl

import com.practicum.playlistmaker.edittracks.domain.EditTracksInteractor
import com.practicum.playlistmaker.edittracks.domain.EditTracksRepository
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.playlist.domain.PlayListRepository
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import kotlinx.coroutines.flow.Flow

class EditTracksInteractorImpl(
    private val editTracksRepository: EditTracksRepository,
    private val navigator: ExternalNavigator
) :
    EditTracksInteractor {
    override suspend fun getTracks(playlistId: Int): Flow<List<Track>> {
        return editTracksRepository.getTracks(playlistId)
    }

    override suspend fun deleteTrack(playlistId: Int, trackId: Int) {
        editTracksRepository.deleteTrack(playlistId, trackId)
    }

    override suspend fun deleteList(playlistId: Int) {
        editTracksRepository.deleteList(playlistId)
    }

    override fun share(text: String) {
        navigator.share(text)
    }

}