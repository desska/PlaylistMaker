package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.media.data.converters.TrackDbConverter
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
            appDatabase.favoriteDao().insert(trackDbConverter.map(track, addDate))
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
}