package com.practicum.playlistmaker.presentation.ui

import android.app.Activity
import android.widget.ImageButton
import android.widget.TextView
import com.practicum.playlistmaker.R

fun Activity.initializeToolbar(titleId: Int) {
    val buttonArrowBack = findViewById<ImageButton>(R.id.toolbar_button_arrow_back)
    val titleView = findViewById<TextView>(R.id.toolbar_text)

    buttonArrowBack.setOnClickListener { finish() }
    titleView.text = getString(titleId)
}