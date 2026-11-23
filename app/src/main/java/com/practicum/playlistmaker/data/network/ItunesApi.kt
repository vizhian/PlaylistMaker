package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.TracksDtoListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("search?entity=song")
    fun getTrackList(@Query("term") term: String): Call<TracksDtoListResponse>
}