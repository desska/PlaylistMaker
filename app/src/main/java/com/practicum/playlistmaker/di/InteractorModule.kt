package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import com.practicum.playlistmaker.favorite.domain.impl.FavoriteInteractorImpl
import com.practicum.playlistmaker.newlist.domain.NewListInteractor
import com.practicum.playlistmaker.newlist.domain.impl.NewListInteractorImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.impl.PlayListInteractorImpl
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

    single<NewListInteractor> {
        NewListInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlayListInteractorImpl(get())
    }

}
