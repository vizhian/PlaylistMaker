package com.practicum.playlistmaker.presentation

import androidx.appcompat.app.AppCompatDelegate

class ThemeApplier {
    fun applyTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}