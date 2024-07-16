package com.practicum.playlistmaker.newlist.domain

import com.practicum.playlistmaker.newlist.domain.entity.Playlist

interface NewListInteractor {
    suspend fun addList(playlist: Playlist)
}