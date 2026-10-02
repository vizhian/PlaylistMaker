package com.practicum.playlistmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SearchActivityPresenter(
    private val repository: Repository,
    private val historyStorage: SearchHistoryStorage
) {
    private val _screenState: MutableLiveData<SearchScreenState> =
        MutableLiveData(SearchScreenState.Start)
    val screenState: LiveData<SearchScreenState> = _screenState

    fun updateTrackList(searchString: String) {
        if (searchString.isEmpty()) {
            _screenState.value = SearchScreenState.Start
            return
        }

        repository.updateTrackList(searchString) { repositoryResult ->
            when (repositoryResult) {
                is RepositoryResult.Error -> _screenState.value = SearchScreenState.Error
                is RepositoryResult.Success -> {
                    if (repositoryResult.trackList.isEmpty()) {
                        _screenState.value = SearchScreenState.Empty
                    } else {
                        _screenState.value =
                            SearchScreenState.Successful(repositoryResult.trackList)
                    }
                }
            }
        }
    }

    fun showHistory() {
        val trackHistory = historyStorage.tracks
        if (!trackHistory.isEmpty()) {
            _screenState.value = SearchScreenState.History(trackHistory)
        }
    }

    fun addTrackToHistory(track: Track) {
        historyStorage.addTrack(track)
    }

    fun clearTrackHistory() {
        historyStorage.clearAll()
        _screenState.value = SearchScreenState.Start
    }
}

sealed class SearchScreenState {
    object Start : SearchScreenState()
    class Successful(val trackList: List<Track>) : SearchScreenState()
    object Empty : SearchScreenState()
    object Error : SearchScreenState()
    class History(val trackList: List<Track>) : SearchScreenState()
}