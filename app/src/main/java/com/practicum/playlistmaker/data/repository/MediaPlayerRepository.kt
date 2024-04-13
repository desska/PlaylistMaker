package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.PlayerState
import com.practicum.playlistmaker.domain.api.PlayerRepository

class MediaPlayerRepository : PlayerRepository {

    override var state = PlayerState.STATE_DEFAULT

    private val player = MediaPlayer()

    override fun prepare(url: String, listener: PlayerRepository.OnPreparedListener) {

        if (url == "") {

            state = PlayerState.STATE_URL_ERROR
            return
        }

        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {

            state = PlayerState.STATE_PAUSED
            listener.onPrepared()

        }

        player.setOnCompletionListener {

            state = PlayerState.STATE_PREPARED
            listener.onComplete()

        }


    }

    override fun start() {

        if (state == PlayerState.STATE_URL_ERROR) {
            return
        }

        player.start()
        state = PlayerState.STATE_PLAYING

    }

    override fun pause() {

        if (state == PlayerState.STATE_URL_ERROR) {

            return

        }

        player.pause()
        state = PlayerState.STATE_PAUSED

    }

    override fun release() {

        if (state != PlayerState.STATE_URL_ERROR) {

            player.release()

        }

    }

    override fun getCurrentPosition(): Int = player.currentPosition

}