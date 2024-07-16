package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface PlayerInteractor {

    interface OnPreparedListener{

        fun onPrepared()
        fun onComplete()
    }

    fun prepare(url: String, listener: OnPreparedListener)

    fun start()

    fun pause()

    fun release()

    fun getCurrentPosition(): Int

    suspend fun addToFavorite(track: Track, addDate: Date)

    suspend fun removeFromFavorite(trackId: Int?)

    suspend fun isInFavorite(trackId: Int?): Flow<Boolean>

    suspend fun isInPlaylist(trackId: Int, playlistId: Int): Flow<Boolean>

    suspend fun addToPlaylist(trackId: Int, playlistId: Int)
}