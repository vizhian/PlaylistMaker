package com.practicum.playlistmaker.domain.api
import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun getTracks(): List<Track>
    fun putTracks(tracks: List<Track>)
}