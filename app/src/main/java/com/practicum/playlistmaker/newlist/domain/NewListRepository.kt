package com.practicum.playlistmaker.newlist.domain

import com.practicum.playlistmaker.newlist.domain.entity.Playlist

interface NewListRepository {
    suspend fun addList(list: Playlist)

}