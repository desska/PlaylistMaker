package com.practicum.playlistmaker.player.data

interface PlayerRepository {

    interface OnPreparedListener{

        fun onPrepared()
        fun onComplete()
    }

    fun getCurrentPosition(): Int

    fun prepare(url: String, listener: OnPreparedListener)

    fun start()

    fun pause()

    fun release()

}