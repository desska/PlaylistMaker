package com.practicum.playlistmaker.search.data.entity

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String
    ): Call<TrackSearchResponse>

}