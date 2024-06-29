package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.TrackEntity
import com.practicum.playlistmaker.media.data.converters.TrackDbConverter
import com.practicum.playlistmaker.media.domain.FavoriteRepository
import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoriteRepository {

    override fun getFavorite(
    ): Flow<List<Track>> {
        return flow {
            val tracks = appDatabase.favoriteDao().getAll()
            emit(convertFromTrackEntity(tracks))
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

}