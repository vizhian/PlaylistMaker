package com.practicum.playlistmaker.data.storage

import android.content.Context
import android.util.Log
import com.practicum.playlistmaker.data.api.StorageClientBoolean
import androidx.core.content.edit
import com.practicum.playlistmaker.data.api.StorageClientString

class SharedPreferencesImpl(context: Context, prefsName: String) : StorageClientBoolean,
    StorageClientString {
    private val sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    override fun getBoolean(key: String): Boolean {
        try {
            return sharedPreferences.getBoolean(key, false)
        } catch (e: ClassCastException) {
            Log.w("SharedPreferencesImpl", "error getBoolean", e)
            return false
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    override fun getString(key: String): String {
        try {
            return sharedPreferences.getString(key, "")?:""
        } catch (e: ClassCastException) {
            Log.w("SharedPreferencesImpl", "error getString", e)
            sharedPreferences.edit { remove(key) }
            return ""
        }
    }

    override fun putString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }
}