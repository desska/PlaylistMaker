package com.practicum.playlistmaker.search.data.entity

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    @GET("/search?entity=song")
    suspend fun search(
        @Query("term") text: String
    ): TrackSearchResponse

}