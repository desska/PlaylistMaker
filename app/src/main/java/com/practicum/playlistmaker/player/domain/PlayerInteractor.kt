package com.practicum.playlistmaker.player.domain

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

}