package com.practicum.playlistmaker.domain.api

interface DarkThemeRepository {
    fun getDarkTheme(): Boolean
    fun putDarkTheme(value: Boolean)
}