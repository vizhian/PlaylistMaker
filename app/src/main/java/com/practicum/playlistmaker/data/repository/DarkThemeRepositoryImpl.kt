package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.api.StorageClientBoolean
import com.practicum.playlistmaker.domain.api.DarkThemeRepository

class DarkThemeRepositoryImpl(private val client: StorageClientBoolean) : DarkThemeRepository {
    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
    }

    override fun getDarkTheme(): Boolean {
        return client.getBoolean(KEY_DARK_THEME)
    }

    override fun putDarkTheme(value: Boolean) {
        client.putBoolean(KEY_DARK_THEME, value)
    }
}