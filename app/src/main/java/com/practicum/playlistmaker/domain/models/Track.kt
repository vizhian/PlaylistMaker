package com.practicum.playlistmaker.domain.models

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {
    val trackTime: String
        get() = formatIntToMMSS(trackTimeMillis)

    val releaseYear: String
        get() = runCatching { Instant.parse(releaseDate).atZone(ZoneId.of("UTC")).year.toString() }
            .getOrDefault("")

    val artworkUrl512: String
        get() = artworkUrl100.substringBeforeLast("/") + "/" +
                artworkUrl100.substringAfterLast("/").replace("100x100bb", "512x512bb")

    companion object {
        private val timeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

        fun formatIntToMMSS(trackTimeMillis: Int): String {
            synchronized(timeFormatter) {
                return timeFormatter.format(trackTimeMillis)
            }
        }
    }
}
