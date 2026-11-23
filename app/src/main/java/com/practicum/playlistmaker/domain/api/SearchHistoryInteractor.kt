package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track
import kotlin.collections.List

interface SearchHistoryInteractor {
    val tracks: List<Track>
    fun addTrack(track: Track)
    fun clearAll()
}