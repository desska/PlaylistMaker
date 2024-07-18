package com.practicum.playlistmaker.playlist.data.impl

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.newlist.data.converters.PlayListDbConverter
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.playlist.domain.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlayListDbConverter
) : PlayListRepository {
    override fun getLists(): Flow<List<Playlist>> {
        return flow {
            val tracks = appDatabase.playlistDao().getLists().map {
                converter.map(it)
            }
            emit(tracks)
        }.flowOn(Dispatchers.IO)
    }

}