package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.search.domain.SearchServiceRepository
import com.practicum.playlistmaker.search.data.impl.SearchServiceRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.HistoryLocalStorage
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(getPlayerRepository())

    fun provideSearchInteractor(context: Context): SearchInteractor = SearchInteractorImpl(getSearchRepository(context = context))

    fun provideSharingInteractor(context: Context): SharingInteractor = SharingInteractorImpl(navigator = getExternalNavigator(context))

    fun ProvideSettingsInteractor(context: Context): SettingsInteractor = SettingsInteractorImpl(
        getSettingsRepository(context = context)
    )

    private fun getPlayerRepository(): PlayerRepository = PlayerRepositoryImpl()

    private fun getSearchRepository(context: Context): SearchServiceRepository =
        SearchServiceRepositoryImpl(
            HistoryLocalStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE))
        )

    private fun getSettingsRepository(context: Context): SettingsRepository = SettingsRepositoryImpl(context = context)

    private fun getExternalNavigator(context: Context): ExternalNavigator = ExternalNavigatorImpl(context = context)

}