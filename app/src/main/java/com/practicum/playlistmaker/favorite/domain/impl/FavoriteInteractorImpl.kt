package com.practicum.playlistmaker.favorite.domain.impl

import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import com.practicum.playlistmaker.favorite.domain.FavoriteRepository
import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) :
    FavoriteInteractor {

    override fun getFavorite(): Flow<List<Track>> {
        return favoriteRepository.getFavorite()
    }
}