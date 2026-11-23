package com.practicum.playlistmaker.data.dto

sealed interface NetworkClientResponse<out T> {
    class Success<T>(val data: T): NetworkClientResponse<T>
    object Error: NetworkClientResponse<Nothing>
}