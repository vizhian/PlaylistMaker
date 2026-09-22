package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
) {
    val trackTime: String
        get() = format(trackTimeMillis)

    companion object {
        private val timeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

        fun format(trackTimeMillis: Int): String {
            synchronized(timeFormatter) {
                return timeFormatter.format(trackTimeMillis)
            }
        }
    }
}
