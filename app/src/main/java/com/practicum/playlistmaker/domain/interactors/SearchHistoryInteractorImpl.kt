package com.practicum.playlistmaker.domain.interactors

import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository): SearchHistoryInteractor {
    private val _tracks: MutableList<Track> = searchHistoryRepository.getTracks().toMutableList()
    override val tracks: List<Track>
        get() = _tracks.toList()

    override fun addTrack(track: Track) {
        _tracks.removeAll { it.trackId == track.trackId }

        _tracks.add(0, track)
        if (_tracks.size > MAX_SIZE) {
            _tracks.removeAt(_tracks.lastIndex)
        }

        searchHistoryRepository.putTracks(_tracks)
    }

    override fun clearAll() {
        _tracks.clear()
        searchHistoryRepository.putTracks(_tracks)
    }

    companion object {
        private const val MAX_SIZE = 10
    }
}