package com.practicum.playlistmaker.data.api

interface StorageClientBoolean {
    fun getBoolean(key: String): Boolean
    fun putBoolean(key: String, value: Boolean)
}