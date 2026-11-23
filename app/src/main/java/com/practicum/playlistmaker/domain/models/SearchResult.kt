package com.practicum.playlistmaker.domain.models

sealed interface SearchResult<out T> {
    class Success<T>(val data: T) : SearchResult<T>
    object Error: SearchResult<Nothing>

}