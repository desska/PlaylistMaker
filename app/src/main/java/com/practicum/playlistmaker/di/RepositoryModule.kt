package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.search.data.impl.SearchServiceRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchServiceRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    single<SearchServiceRepository> {
        SearchServiceRepositoryImpl(
            get(),
            get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(themeQualifier))
    }
}
