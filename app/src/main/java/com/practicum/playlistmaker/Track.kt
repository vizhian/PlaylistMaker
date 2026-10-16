package com.practicum.playlistmaker

import android.os.Parcel
import android.os.Parcelable
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
    val country: String
) : Parcelable {
    val trackTime: String
        get() = formatIntToMMSS(trackTimeMillis)

    val releaseYear: String
        get() = runCatching { Instant.parse(releaseDate).atZone(ZoneId.of("UTC")).year.toString() }
            .getOrDefault("")

    val artworkUrl525: String
        get() = artworkUrl100.substringBeforeLast("/") + "/" +
                artworkUrl100.substringAfterLast("/").replace("100x100bb", "525x525bb")
    //Ручная реализация Parcelable
    constructor(source: Parcel) : this(
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readInt(),
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: ""
    )
    //Ручная реализация Parcelable
    override fun describeContents(): Int = 0
    //Ручная реализация Parcelable
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(trackId)
        dest.writeString(trackName)
        dest.writeString(artistName)
        dest.writeInt(trackTimeMillis)
        dest.writeString(artworkUrl100)
        dest.writeString(collectionName)
        dest.writeString(releaseDate)
        dest.writeString(primaryGenreName)
        dest.writeString(country)
    }

    companion object CREATOR : Parcelable.Creator<Track> {

        const val EXTRA_TRACK = "track"

        private val timeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

        fun formatIntToMMSS(trackTimeMillis: Int): String {
            synchronized(timeFormatter) {
                return timeFormatter.format(trackTimeMillis)
            }
        }

        //Ручная реализация Parcelable
        override fun createFromParcel(source: Parcel): Track = Track(source)

        override fun newArray(size: Int): Array<Track?> = arrayOfNulls(size)

    }

}
