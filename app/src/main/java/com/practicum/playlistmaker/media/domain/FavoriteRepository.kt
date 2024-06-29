package com.practicum.playlistmaker.media.domain

import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getFavorite(): Flow<List<Track>>

}