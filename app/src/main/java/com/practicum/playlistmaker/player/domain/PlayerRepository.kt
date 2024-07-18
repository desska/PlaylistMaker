package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface PlayerRepository {
    interface OnPreparedListener {
        fun onPrepared()
        fun onComplete()
    }

    fun getCurrentPosition(): Int

    fun prepare(url: String, listener: OnPreparedListener)

    fun start()

    fun pause()

    fun release()

    suspend fun addToFavorite(track: Track, addDate: Date)

    suspend fun removeFromFavorite(trackId: Int?)

    suspend fun isInFavorite(trackId: Int?): Flow<Boolean>

    suspend fun isInPlaylist(trackId: Int, playlistId: Int): Flow<Boolean>

    suspend fun addToPlaylist(track: Track, playlistId: Int)

}