package com.practicum.playlistmaker.di

import android.content.Context
import com.practicum.playlistmaker.data.network.AudioPlayerImpl
import com.practicum.playlistmaker.data.network.RetrofitItunesImpl
import com.practicum.playlistmaker.data.repository.DarkThemeRepositoryImpl
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.SearchTrackRepositoryImpl
import com.practicum.playlistmaker.data.storage.SharedPreferencesImpl
import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.DarkThemeInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchTrackInteractor
import com.practicum.playlistmaker.domain.interactors.AudioPlayerInteractorImp
import com.practicum.playlistmaker.domain.interactors.DarkThemeInteractorImpl
import com.practicum.playlistmaker.domain.interactors.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.interactors.SearchInteractorImpl
import com.practicum.playlistmaker.presentation.ThemeApplier
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Creator {
    private const val PREFS_APP_NAME = "my_app_preferences"
    private const val PREFS_HISTORY_NAME = "search_history_storage"

    private const val SEARCH_URL = "https://itunes.apple.com/"

    private lateinit var appContext: Context
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    val darkThemeInteractor: DarkThemeInteractor by lazy {
        check(::appContext.isInitialized) { "Creator.init() not called" }
        DarkThemeInteractorImpl(
            DarkThemeRepositoryImpl(
                SharedPreferencesImpl(
                    appContext,
                    PREFS_APP_NAME
                )
            )
        )
    }

    val themeApplier by lazy { ThemeApplier() }

    val SearchHistoryInteractor: SearchHistoryInteractor by lazy {
        SearchHistoryInteractorImpl(
            SearchHistoryRepositoryImpl(
                SharedPreferencesImpl(appContext, PREFS_HISTORY_NAME)
            )
        )
    }

    val executor: Executor by lazy { Executors.newCachedThreadPool() }

    val searchInteractor: SearchTrackInteractor by lazy {
        SearchInteractorImpl(SearchTrackRepositoryImpl(RetrofitItunesImpl(SEARCH_URL)), executor)
    }

    fun audioPlayer(url: String): AudioPlayerInteractor {
        return AudioPlayerInteractorImp(AudioPlayerImpl(url))
    }

}