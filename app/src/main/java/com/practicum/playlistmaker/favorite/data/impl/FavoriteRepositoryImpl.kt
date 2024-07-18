package com.practicum.playlistmaker.favorite.data.impl

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.FavoriteEntity
import com.practicum.playlistmaker.favorite.data.converters.FavoriteDbConverter
import com.practicum.playlistmaker.favorite.domain.FavoriteRepository
import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteDbConverter: FavoriteDbConverter
) : FavoriteRepository {

    override fun getFavorite(
    ): Flow<List<Track>> {
        return flow {
            val tracks = appDatabase.favoriteDao().getAll()
            emit(convertFromTrackEntity(tracks))
        }.flowOn(Dispatchers.IO)
    }

    private fun convertFromTrackEntity(tracks: List<FavoriteEntity>): List<Track> {
        return tracks.map { track -> favoriteDbConverter.map(track) }
    }

}