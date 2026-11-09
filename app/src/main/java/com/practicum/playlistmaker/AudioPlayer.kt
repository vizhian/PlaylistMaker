package com.practicum.playlistmaker

import android.media.MediaPlayer

class AudioPlayer(val url: String) {

    private val player = MediaPlayer()
    var state = STATE_DEFAULT
        private set

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val STATE_RELEASED = 4
    }

    fun preparePlayer(
        prepareListener: () -> Unit,
        completionListener: () -> Unit,
        errorListener: (what: Int, extra: Int) -> Unit,
    ) {
        if (state == STATE_RELEASED) return

        try {
            player.setDataSource(url)
        } catch(_: Exception) {
            state = STATE_RELEASED
            errorListener(0, 0)
            return
        }

        player.setOnPreparedListener {
            if (playerReleased()) return@setOnPreparedListener
            state = STATE_PREPARED
            prepareListener()
        }

        player.setOnCompletionListener {
            if (playerReleased()) return@setOnCompletionListener
            state = STATE_PREPARED
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

    fun playback() {
        if (state == STATE_RELEASED) return
        when (state) {
            STATE_PLAYING -> pause()
            STATE_PAUSED, STATE_PREPARED -> play()
        }
    }

    fun play() {
        if (state != STATE_PAUSED && state != STATE_PREPARED) return
        player.start()
        state = STATE_PLAYING
    }

    fun pause() {
        if (state != STATE_PLAYING) return
        player.pause()
        state = STATE_PAUSED
    }

    fun releasePlayer() {
        if (state == STATE_RELEASED) return
        state = STATE_RELEASED
        player.release()
    }

    fun getCurrentPosition (): Int {
        if (state == STATE_RELEASED) return -1
        return player.currentPosition
    }

    private fun playerReleased(): Boolean = state == STATE_RELEASED
}