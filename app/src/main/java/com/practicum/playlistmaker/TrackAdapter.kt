package com.practicum.playlistmaker

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    private val trackList = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_list_element, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount() = trackList.size

    fun updateTrackList(newTrackList: List<Track>) {
        trackList.clear()
        trackList.addAll(newTrackList)
        notifyDataSetChanged()
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackNameView = itemView.findViewById<TextView>(R.id.track_name)
        private val artistNameView = itemView.findViewById<TextView>(R.id.artist_name)
        private val trackTimeView = itemView.findViewById<TextView>(R.id.track_time)
        private val trackArtView = itemView.findViewById<ImageView>(R.id.track_art)

        private val dpRadius: RoundedCorners = RoundedCorners(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2f,
                itemView.context.resources.displayMetrics
            ).toInt()
        )

        fun bind(track: Track) {
            trackNameView.text = track.trackName
            artistNameView.text = track.artistName
            trackTimeView.text = track.trackTime

            Glide.with(trackArtView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(dpRadius)
                .fitCenter()
                .into(trackArtView)
        }
    }
}