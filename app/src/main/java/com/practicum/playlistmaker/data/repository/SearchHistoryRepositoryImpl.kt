package com.practicum.playlistmaker.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.practicum.playlistmaker.data.api.StorageClientString
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class SearchHistoryRepositoryImpl(private val storageClientString: StorageClientString) :
    SearchHistoryRepository {
    private val gson = Gson()

    companion object {
        private const val KEY_SEARCH_HISTORY = "history"
    }

    override fun getTracks(): List<Track> {

        val json = storageClientString.getString(KEY_SEARCH_HISTORY)

        if (json.isEmpty()) return listOf()

        try {
            return gson.fromJson(json, Array<Track>::class.java)?.toList() ?: listOf()
        } catch (e: JsonSyntaxException) {
            Log.w("SearchHistoryRepositoryImpl", "getTracks", e)
            return listOf()
        }
    }

    override fun putTracks(tracks: List<Track>) {
        storageClientString.putString(KEY_SEARCH_HISTORY, gson.toJson(tracks))
    }
}