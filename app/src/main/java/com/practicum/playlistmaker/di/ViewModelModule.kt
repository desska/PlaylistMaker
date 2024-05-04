package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModuleModule = module {

    viewModel {

            (track: Track) ->
        PlayerViewModel(track, get())

    }

    viewModel {

        SearchViewModel(get())

    }

    viewModel {

        SettingsViewModel(get(), get())

    }


}
