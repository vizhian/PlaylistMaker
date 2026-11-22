package com.practicum.playlistmaker.domain.interactors

import com.practicum.playlistmaker.domain.api.AudioPlayer
import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.AudioPlayerState

class AudioPlayerInteractorImp(val audioPlayer: AudioPlayer) : AudioPlayerInteractor {
    override val state get() = audioPlayer.state

    override fun preparePlayer(
        prepareListener: () -> Unit,
        completionListener: () -> Unit,
        errorListener: (what: Int, extra: Int) -> Unit,
    ) {
        audioPlayer.preparePlayer(prepareListener, completionListener, errorListener)
    }

    override fun playback() {
        when (state) {
            AudioPlayerState.PLAYING -> pause()
            AudioPlayerState.PAUSED, AudioPlayerState.PREPARED -> play()
            else -> return
        }
    }

    override fun play() = audioPlayer.play()
    override fun pause() = audioPlayer.pause()
    override fun releasePlayer() = audioPlayer.releasePlayer()
    override fun getCurrentPosition() = audioPlayer.getCurrentPosition()
}