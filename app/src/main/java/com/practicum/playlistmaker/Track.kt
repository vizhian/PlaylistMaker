package com.practicum.playlistmaker

import android.os.Parcelable
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) : Parcelable {
    val trackTime: String
        get() = formatIntToMMSS(trackTimeMillis)

    val releaseYear: String
        get() = runCatching { Instant.parse(releaseDate).atZone(ZoneId.of("UTC")).year.toString() }
            .getOrDefault("")

    val artworkUrl512: String
        get() = artworkUrl100.substringBeforeLast("/") + "/" +
                artworkUrl100.substringAfterLast("/").replace("100x100bb", "512x512bb")

    companion object {
        const val EXTRA_TRACK = "track"

        private val timeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

        fun formatIntToMMSS(trackTimeMillis: Int): String {
            synchronized(timeFormatter) {
                return timeFormatter.format(trackTimeMillis)
            }
        }
    }
}
