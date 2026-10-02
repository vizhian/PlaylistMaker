package com.practicum.playlistmaker

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class SearchHistoryStorage(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    private val _tracks: MutableList<Track> = loadTracksFromPrefs()

    val tracks: List<Track>
        get() = _tracks.toList()

    fun addTrack(track: Track) {
        _tracks.removeAll { it.trackId == track.trackId }

        _tracks.add(0, track)
        if (_tracks.size > MAX_SIZE) {
            _tracks.removeAt(_tracks.lastIndex)
        }

        loadTracksToPrefs()
    }

    fun clearAll() {
        _tracks.clear()
        loadTracksToPrefs()
    }

    private fun loadTracksFromPrefs(): MutableList<Track> {
        val json = try {
            preferences.getString(KEY_TRACK_ARRAY, null)
        } catch (e: ClassCastException) {
            Log.w(TAG, "History stored with wrong type, resetting", e)
            preferences.edit().remove(KEY_TRACK_ARRAY).apply()
            return mutableListOf()
        }

        if (json.isNullOrEmpty()) return mutableListOf()

        try {
            return gson.fromJson(json, Array<Track>::class.java)?.toMutableList() ?: mutableListOf()
        } catch (e: JsonSyntaxException) {
            Log.w(TAG, "Corrupted history, resetting", e)
            return mutableListOf()
        }
    }

    private fun loadTracksToPrefs() {
        preferences.edit().putString(KEY_TRACK_ARRAY, gson.toJson(_tracks)).apply()
    }

    companion object {
        private const val PREFS_NAME = "search_history_storage"
        private const val KEY_TRACK_ARRAY = "track_array"
        private const val MAX_SIZE = 10
        private const val TAG = "SearchHistoryStorage"
    }
}