package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.di.Creator

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
        Creator.themeApplier.applyTheme(Creator.darkThemeInteractor.getDarkTheme())
    }
}