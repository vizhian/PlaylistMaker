package com.practicum.playlistmaker.domain.interactors

import com.practicum.playlistmaker.domain.api.DarkThemeInteractor
import com.practicum.playlistmaker.domain.api.DarkThemeRepository

class DarkThemeInteractorImpl(private val repository: DarkThemeRepository): DarkThemeInteractor {
    override fun getDarkTheme(): Boolean {
        return repository.getDarkTheme()
    }
    override fun putDarkTheme(value: Boolean) {
        repository.putDarkTheme(value)
    }
}