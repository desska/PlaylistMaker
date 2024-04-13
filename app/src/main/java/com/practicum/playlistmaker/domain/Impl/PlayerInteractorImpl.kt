package com.practicum.playlistmaker.domain.Impl

import com.practicum.playlistmaker.PlayerState
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerRepository

class PlayerInteractorImpl(val playerRepository: PlayerRepository): PlayerInteractor {


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

    override fun getState(): PlayerState = playerRepository.state

    override fun start() = playerRepository.start()

    override fun pause() = playerRepository.pause()

    override fun release() = playerRepository.release()

    override fun getCurrentPosition(): Int = playerRepository.getCurrentPosition()
}