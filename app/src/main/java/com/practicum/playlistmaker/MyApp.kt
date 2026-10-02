package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MyApp : Application() {
    val repository by lazy { Repository() }

    val appPreferences by lazy {MyAppPreferences(this)}

    val historyStorage by lazy { SearchHistoryStorage(this) }

    override fun onCreate() {
        super.onCreate()
        applyTheme(appPreferences.darkTheme)
    }

    fun isDarkTheme() = appPreferences.darkTheme

    fun switchTheme(darkThemeEnabled: Boolean) {
        appPreferences.darkTheme = darkThemeEnabled
        applyTheme(darkThemeEnabled)
    }

    private fun applyTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}