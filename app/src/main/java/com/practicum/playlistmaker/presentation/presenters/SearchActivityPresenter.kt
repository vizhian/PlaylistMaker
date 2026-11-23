package com.practicum.playlistmaker.presentation.presenters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchTrackInteractor
import com.practicum.playlistmaker.domain.models.SearchResult
import com.practicum.playlistmaker.domain.models.Track

class SearchActivityPresenter(
    private val historyInteractor: SearchHistoryInteractor,
    private val searchInteractor: SearchTrackInteractor
) {
    private val _screenState: MutableLiveData<SearchScreenState> =
        MutableLiveData(SearchScreenState.Start)
    val screenState: LiveData<SearchScreenState> get() = _screenState

    fun updateTrackList(searchString: String) {
        if (searchString.isEmpty()) {
            _screenState.value = SearchScreenState.Start
            return
        }

        _screenState.value = SearchScreenState.InProgress

        searchInteractor.searchTracks(searchString) { searchResult ->
            when (searchResult) {
                is SearchResult.Error -> _screenState.postValue(SearchScreenState.Error)
                is SearchResult.Success -> {
                    if (searchResult.data.isEmpty()) {
                        _screenState.postValue(SearchScreenState.Empty)
                    } else {
                        _screenState.postValue(
                            SearchScreenState.Successful(searchResult.data))
                    }
                }
            }
        }
    }

    fun showHistory() {
        if (historyInteractor.tracks.isNotEmpty()) {
            _screenState.value = SearchScreenState.History(historyInteractor.tracks)
        }
    }

    fun addTrackToHistory(track: Track) {
        historyInteractor.addTrack(track)
    }

    fun clearTrackHistory() {
        historyInteractor.clearAll()
        _screenState.value = SearchScreenState.Start
    }
}

sealed class SearchScreenState {
    object Start : SearchScreenState()
    object InProgress : SearchScreenState()
    class Successful(val trackList: List<Track>) : SearchScreenState()
    object Empty : SearchScreenState()
    object Error : SearchScreenState()
    class History(val trackList: List<Track>) : SearchScreenState()
}