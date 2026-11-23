package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.SearchResult
import com.practicum.playlistmaker.domain.models.Track

interface SearchTrackInteractor {
    fun searchTracks(term: String, callback: (SearchResult<List<Track>>) -> Unit)
}