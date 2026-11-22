package com.practicum.playlistmaker.data.api

import com.practicum.playlistmaker.data.dto.NetworkClientResponse
import com.practicum.playlistmaker.data.dto.TrackDto

interface NetworkClient {
    fun getTracks(term: String): NetworkClientResponse<List<TrackDto>>
}