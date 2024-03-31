package com.practicum.playlistmaker

import com.practicum.playlistmaker.domain.entity.Track

class TrackSearchResponse(
    val resultCount: Int,
    val results: List<Track>
)