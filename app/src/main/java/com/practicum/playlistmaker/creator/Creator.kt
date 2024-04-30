package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.data.PlayerRepository
import com.practicum.playlistmaker.search.data.SearchServiceRepository
import com.practicum.playlistmaker.search.data.impl.SearchServiceRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.player.data.impl.MediaPlayerRepository
import com.practicum.playlistmaker.search.data.impl.HistoryLocalStorage
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.data.impl.ExternalNavigator
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(getPlayerRepository())

    fun provideSearchInteractor(context: Context): SearchInteractor = SearchInteractorImpl(getSearchRepository(context = context))

    fun provideSharingInteractor(context: Context): SharingInteractor = SharingInteractorImpl(context = context, navigator = ExternalNavigator(context))

    fun ProvideSettingsInteractor(context: Context): SettingsInteractor = SettingsInteractorImpl(
        getSettingsRepository(context = context)
    )

    private fun getPlayerRepository(): PlayerRepository = MediaPlayerRepository()

    private fun getSearchRepository(context: Context): SearchServiceRepository =
        SearchServiceRepositoryImpl(
            HistoryLocalStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE))
        )

    private fun getSettingsRepository(context: Context): SettingsRepository = SettingsRepositoryImpl(context = context)

}