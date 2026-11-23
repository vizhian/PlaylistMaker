package com.practicum.playlistmaker.domain.api

interface AudioPlayerInteractor {
    val state: AudioPlayerState
    fun preparePlayer(
        prepareListener: () -> Unit,
        completionListener: () -> Unit,
        errorListener: (what: Int, extra: Int) -> Unit,
    )

    fun playback()

    fun play()

    fun pause()

    fun releasePlayer()

    fun getCurrentPosition(): Int
}