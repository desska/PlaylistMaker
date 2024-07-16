package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import java.util.Date

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {
    override fun prepare(url: String, listener: PlayerInteractor.OnPreparedListener) {
        playerRepository.prepare(url, object : PlayerRepository.OnPreparedListener {
            override fun onPrepared() {
                listener.onPrepared()
            }

            override fun onComplete() {
                listener.onComplete()
            }

        })

    }

    override fun start() = playerRepository.start()

    override fun pause() = playerRepository.pause()

    override fun release() = playerRepository.release()

    override fun getCurrentPosition(): Int = playerRepository.getCurrentPosition()

    override suspend fun addToFavorite(track: Track, addDate: Date) {
        playerRepository.addToFavorite(track, addDate)
    }

    override suspend fun removeFromFavorite(trackId: Int?) {
        playerRepository.removeFromFavorite(trackId)
    }

    override suspend fun isInFavorite(trackId: Int?): Flow<Boolean> {
        return playerRepository.isInFavorite(trackId)
    }

    override suspend fun isInPlaylist(trackId: Int, playlistId: Int): Flow<Boolean> {
        return playerRepository.isInPlaylist(trackId, playlistId)
    }

    override suspend fun addToPlaylist(trackId: Int, playlistId: Int) {
        playerRepository.addToPlaylist(trackId, playlistId)
    }
}