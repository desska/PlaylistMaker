package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorite.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favorite.data.impl.FavoriteRepositoryImpl
import com.practicum.playlistmaker.favorite.domain.FavoriteRepository
import com.practicum.playlistmaker.newlist.data.converters.PlayListDbConverter
import com.practicum.playlistmaker.newlist.data.impl.NewListRepositoryImpl
import com.practicum.playlistmaker.newlist.domain.NewListRepository
import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.playlist.data.impl.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist.domain.PlayListRepository
import com.practicum.playlistmaker.search.data.impl.SearchServiceRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchServiceRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PlayerRepository> {
        PlayerRepositoryImpl(get(), get(), get())
    }

    single<SearchServiceRepository> {
        SearchServiceRepositoryImpl(
            get(), get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(themeQualifier))
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<NewListRepository> {
        NewListRepositoryImpl(get(), get(), get())
    }

    single<PlayListRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    factory { TrackDbConverter() }

    factory { PlayListDbConverter() }
}
