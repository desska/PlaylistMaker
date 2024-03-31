package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.PlayerState

interface PlayerRepository {

    interface OnPreparedListener{

        fun onPrepared()
        fun onComplete()
    }

    var state: PlayerState

    fun getCurrentPosition(): Int

    fun prepare(url: String, listener: OnPreparedListener)

    fun start()

    fun pause()

    fun release()

}