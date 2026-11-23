package com.practicum.playlistmaker.domain.interactors

import com.practicum.playlistmaker.domain.api.SearchTrackInteractor
import com.practicum.playlistmaker.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.domain.models.SearchResult
import com.practicum.playlistmaker.domain.models.Track
import java.util.concurrent.Executor

class SearchInteractorImpl
    (
    private val searchRepository: SearchTrackRepository,
    private val executor: Executor
) : SearchTrackInteractor {

    override fun searchTracks(term: String, callback: (SearchResult<List<Track>>) -> Unit) {
        executor.execute {
            callback(searchRepository.searchTracks(term))
        }
    }
}
