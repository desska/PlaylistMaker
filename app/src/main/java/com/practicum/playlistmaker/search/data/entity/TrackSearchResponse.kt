package com.practicum.playlistmaker.search.data.entity

import com.practicum.playlistmaker.player.domain.entity.Track

class TrackSearchResponse(
    val resultCount: Int,
    val results: List<Track>
): NetworkResponse()