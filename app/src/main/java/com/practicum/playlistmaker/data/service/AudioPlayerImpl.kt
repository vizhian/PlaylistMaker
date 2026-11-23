package com.practicum.playlistmaker.data.service

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.AudioPlayer
import com.practicum.playlistmaker.domain.api.AudioPlayerState

class AudioPlayerImpl(val url: String): AudioPlayer {

    private val player = MediaPlayer()

    override var state = AudioPlayerState.DEFAULT
        private set

    override fun preparePlayer(
        prepareListener: () -> Unit,
        completionListener: () -> Unit,
        errorListener: (what: Int, extra: Int) -> Unit,
    ) {
        if (state == AudioPlayerState.RELEASED) return

        try {
            player.setDataSource(url)
        } catch(_: Exception) {
            state = AudioPlayerState.RELEASED
            errorListener(0, 0)
            return
        }

        player.setOnPreparedListener {
            if (playerReleased()) return@setOnPreparedListener
            state = AudioPlayerState.PREPARED
            prepareListener()
        }

        player.setOnCompletionListener {
            if (playerReleased()) return@setOnCompletionListener
            state = AudioPlayerState.PREPARED
            completionListener()
        }

        player.setOnErrorListener { _, what, extra ->
            if (playerReleased()) return@setOnErrorListener true
            releasePlayer()
            errorListener(what, extra)
            return@setOnErrorListener true
        }

        player.prepareAsync()
    }

    override fun play() {
        if (state != AudioPlayerState.PAUSED && state != AudioPlayerState.PREPARED) return
        player.start()
        state = AudioPlayerState.PLAYING
    }

    override fun pause() {
        if (state != AudioPlayerState.PLAYING) return
        player.pause()
        state = AudioPlayerState.PAUSED
    }

    override fun releasePlayer() {
        if (state == AudioPlayerState.RELEASED) return
        state = AudioPlayerState.RELEASED
        player.release()
    }

    override fun getCurrentPosition (): Int {
        if (state == AudioPlayerState.RELEASED) return -1
        return player.currentPosition
    }

    private fun playerReleased(): Boolean = state == AudioPlayerState.RELEASED
}