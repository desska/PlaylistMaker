package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.repository.MediaPlayerRepository
import com.practicum.playlistmaker.domain.Impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerRepository

object Creator {

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(getPlayerRepository())

    private fun getPlayerRepository(): PlayerRepository = MediaPlayerRepository()


}