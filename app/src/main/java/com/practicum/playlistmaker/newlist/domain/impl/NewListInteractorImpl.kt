package com.practicum.playlistmaker.newlist.domain.impl

import com.practicum.playlistmaker.newlist.domain.NewListInteractor
import com.practicum.playlistmaker.newlist.domain.NewListRepository
import com.practicum.playlistmaker.newlist.domain.entity.Playlist

class NewListInteractorImpl(private val repository: NewListRepository): NewListInteractor {
    override suspend fun addList(playlist: Playlist) {
        repository.addList(playlist)
    }
}