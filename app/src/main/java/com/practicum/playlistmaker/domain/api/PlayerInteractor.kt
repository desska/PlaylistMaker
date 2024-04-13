package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.PlayerState

interface PlayerInteractor {

    interface OnPreparedListener{

        fun onPrepared()
        fun onComplete()
    }

    fun getState(): PlayerState

    fun prepare(url: String, listener: OnPreparedListener)

    fun start()

    fun pause()

    fun release()

    fun getCurrentPosition(): Int

}