package com.practicum.playlistmaker.edittracks.data.impl

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.TrackDbConverter
import com.practicum.playlistmaker.edittracks.domain.EditTracksRepository
import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class EditTracksRepositoryImpl(
    private val appDatabase: AppDatabase, private val trackDbConverter: TrackDbConverter
) : EditTracksRepository {
    override suspend fun getTracks(playlistId: Int): Flow<List<Track>> {
        return flow {
            val tracks = appDatabase.playlistDao().getTracks(playlistId).map {
                trackDbConverter.map(it)
            }

            emit(tracks)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteTrack(playlistId: Int, trackId: Int) {
        withContext(Dispatchers.IO) {
            appDatabase.playlistDao().deleteTrackFromList(playlistId, trackId)
            val playlistsId = appDatabase.playlistDao().getAnotherPlaylistId(playlistId, trackId)
            if (playlistsId.isEmpty()) {
                appDatabase.trackDao().delete(trackId)
            }
        }
    }

    override suspend fun deleteList(playlistId: Int) {
        withContext(Dispatchers.IO) {
            val tracksToDelete = appDatabase.playlistDao().getTracksNotExistsInLists(playlistId)
            appDatabase.trackDao().delete(tracksToDelete)
            appDatabase.playlistDao().deleteTracksFromList(playlistId)
            appDatabase.playlistDao().deleteList(playlistId)
        }

    }
}