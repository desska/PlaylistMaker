package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.editlist.ui.EditListViewModel
import com.practicum.playlistmaker.edittracks.ui.EditTracksViewModel
import com.practicum.playlistmaker.favorite.ui.FavoriteViewModel
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.playlist.ui.PlaylistViewModel
import com.practicum.playlistmaker.newlist.ui.NewListViewModel
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModuleModule = module {
    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        NewListViewModel(get())
    }

    viewModel { (playlist: Playlist) ->
        EditListViewModel(playlist, get())
    }

    viewModel { (playlistId: Int) ->
        EditTracksViewModel(playlistId, get(), get())
    }


}
