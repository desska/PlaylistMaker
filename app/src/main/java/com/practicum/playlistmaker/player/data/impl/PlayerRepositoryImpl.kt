package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.PlayerRepository

class PlayerRepositoryImpl(private var player: MediaPlayer) : PlayerRepository {

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

    override fun release()  = player.reset()

    override fun getCurrentPosition(): Int = player.currentPosition

}