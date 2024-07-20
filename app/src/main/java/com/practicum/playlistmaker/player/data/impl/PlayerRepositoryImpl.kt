package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.TrackDbConverter
import com.practicum.playlistmaker.favorite.data.converters.FavoriteDbConverter
import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.Date

class PlayerRepositoryImpl(
    private var player: MediaPlayer,
    private val appDatabase: AppDatabase,
    private val favoriteDbConverter: FavoriteDbConverter,
    private val trackDbConverter: TrackDbConverter
) : PlayerRepository {
    override fun prepare(url: String, listener: PlayerRepository.OnPreparedListener) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            listener.onPrepared()
        }

        player.setOnCompletionListener {
            listener.onComplete()
        }

    }

    override fun start() = player.start()

    override fun pause() = player.pause()

    override fun release() = player.reset()

    override fun getCurrentPosition(): Int = player.currentPosition

    override suspend fun addToFavorite(track: Track, addDate: Date) {
        withContext(Dispatchers.IO) {
            appDatabase.favoriteDao().insert(favoriteDbConverter.map(track, addDate))
        }
    }

    override suspend fun removeFromFavorite(trackId: Int?) {
        withContext(Dispatchers.IO) {
            if (trackId != null) {
                appDatabase.favoriteDao().delete(trackId)
            }
        }
    }

    override suspend fun isInFavorite(trackId: Int?): Flow<Boolean> {
        return flow {
            if (trackId != null) {
                val list = appDatabase.favoriteDao().getOne(trackId)
                emit(list.isNotEmpty())
            } else {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun isInPlaylist(trackId: Int, playlistId: Int): Flow<Boolean> {
        return flow {
            val list = appDatabase.playlistDao().getTracksId(playlistId)
            emit(list.find { it == trackId } != null)
        }.flowOn(Dispatchers.IO)

    }

    override suspend fun addToPlaylist(track: Track, playlistId: Int) {
        if (track.trackId == null) {
            return
        }
        val trackEntity = trackDbConverter.map(track)

        withContext(Dispatchers.IO) {
            appDatabase.playlistDao().addTrack(track.trackId, playlistId)
            appDatabase.trackDao().insert(trackEntity)
        }
    }

}