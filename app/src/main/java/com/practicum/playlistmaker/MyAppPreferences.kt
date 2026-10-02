package com.practicum.playlistmaker

import android.content.Context
import android.util.Log

class MyAppPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var darkTheme: Boolean
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_DARK_THEME, value).apply()
        }
        get(){
            try {
                return sharedPreferences.getBoolean(KEY_DARK_THEME, false)
            } catch (e: ClassCastException) {
                Log.w(TAG, "dark theme is wrong type, return false", e)
                return false
            }
        }

    companion object {
        private const val PREFS_NAME = "my_app_preferences"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val TAG = "MyAppPreferences"
    }
}