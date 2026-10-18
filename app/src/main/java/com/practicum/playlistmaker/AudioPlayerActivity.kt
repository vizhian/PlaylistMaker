package com.practicum.playlistmaker

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track

    private val dpRadiusTrackArt: RoundedCorners by lazy {
        RoundedCorners(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                resources.displayMetrics
            ).toInt()
        )
    }

    private val trackArt: ImageView by lazy { findViewById(R.id.track_art) }
    private val trackName: TextView by lazy { findViewById(R.id.track_name) }
    private val artistName: TextView by lazy { findViewById(R.id.artist_name) }
    private val addButton: ImageButton by lazy { findViewById(R.id.add_button) }
    private val playButton: ImageButton by lazy { findViewById(R.id.play_button) }
    private val likeButton: ImageButton by lazy { findViewById(R.id.like_button) }
    private val currentTrackTime: TextView by lazy { findViewById(R.id.current_track_time) }
    private val trackTime: TextView by lazy { findViewById(R.id.track_time) }
    private val trackTimeValue: TextView by lazy { findViewById(R.id.track_time_value) }
    private val trackAlbum: TextView by lazy { findViewById(R.id.track_album) }
    private val trackAlbumValue: TextView by lazy { findViewById(R.id.track_album_value) }
    private val trackYear: TextView by lazy { findViewById(R.id.track_year) }
    private val trackYearValue: TextView by lazy { findViewById(R.id.track_year_value) }
    private val trackGenre: TextView by lazy { findViewById(R.id.track_genre) }
    private val trackGenreValue: TextView by lazy { findViewById(R.id.track_genre_value) }
    private val trackCountry: TextView by lazy { findViewById(R.id.track_country) }
    private val trackCountryValue: TextView by lazy { findViewById(R.id.track_country_value) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!initializeTrack()) {
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audio_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeToolbar(R.string.empty_string)

        uploadTrackArt()
        trackName.text = track.trackName
        artistName.text = track.artistName
        currentTrackTime.text = track.trackTime

        initializeTrackInfoElement(trackTime, trackTimeValue, track.trackTime)
        initializeTrackInfoElement(trackAlbum, trackAlbumValue, track.collectionName)
        initializeTrackInfoElement(trackYear, trackYearValue, track.releaseYear)
        initializeTrackInfoElement(trackGenre, trackGenreValue, track.primaryGenreName)
        initializeTrackInfoElement(trackCountry, trackCountryValue, track.country)
    }

    private fun initializeTrack(): Boolean {
        val trackOrNull = intent.extras?.let {
            BundleCompat.getParcelable(it, Track.EXTRA_TRACK, Track::class.java)
        } ?: return false
        track = trackOrNull
        return true
    }

    private fun initializeTrackInfoElement(
        viewHeading: TextView,
        viewValue: TextView,
        value: String
    ) {
        if (value.isBlank()) {
            viewHeading.visibility = View.GONE
            viewValue.visibility = View.GONE
        } else {
            viewValue.text = value
        }
    }

    private fun uploadTrackArt() {
        Glide.with(trackArt)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.track_placeholder)
            .transform(dpRadiusTrackArt)
            .into(trackArt)
    }
}

