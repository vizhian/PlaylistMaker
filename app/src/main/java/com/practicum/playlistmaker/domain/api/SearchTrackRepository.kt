package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.SearchResult
import com.practicum.playlistmaker.domain.models.Track

interface SearchTrackRepository {
    fun searchTracks(term: String): SearchResult<List<Track>>
}