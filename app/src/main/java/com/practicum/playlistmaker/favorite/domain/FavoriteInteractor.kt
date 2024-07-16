package com.practicum.playlistmaker.favorite.domain

import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    fun getFavorite(): Flow<List<Track>>

}