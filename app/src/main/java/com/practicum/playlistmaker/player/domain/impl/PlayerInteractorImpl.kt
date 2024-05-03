package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {


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
}