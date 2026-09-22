package com.practicum.playlistmaker

import android.app.Application

class MyApp : Application() {
    val repository by lazy { Repository() }
}