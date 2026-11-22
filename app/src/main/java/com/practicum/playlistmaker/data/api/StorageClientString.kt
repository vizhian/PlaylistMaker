package com.practicum.playlistmaker.data.api

interface StorageClientString {
    fun getString(key: String): String
    fun putString(key: String, value: String)
}