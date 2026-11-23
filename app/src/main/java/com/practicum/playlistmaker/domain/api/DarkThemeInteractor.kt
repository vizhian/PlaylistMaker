package com.practicum.playlistmaker.domain.api

interface DarkThemeInteractor {
    fun getDarkTheme(): Boolean
    fun putDarkTheme(value: Boolean)
}