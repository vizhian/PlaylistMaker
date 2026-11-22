package com.practicum.playlistmaker.domain.api

interface AudioPlayer {
    val state: AudioPlayerState
    fun preparePlayer(
        prepareListener: () -> Unit,
        completionListener: () -> Unit,
        errorListener: (what: Int, extra: Int) -> Unit,
    )

    fun play()

    fun pause()

    fun releasePlayer()

    fun getCurrentPosition(): Int
}

enum class AudioPlayerState {
    DEFAULT,
    PREPARED,
    PLAYING,
    PAUSED,
    RELEASED
}