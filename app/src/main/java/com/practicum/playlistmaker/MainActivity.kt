package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setButtonNavigation(R.id.main_button_search, SearchActivity::class)
        setButtonNavigation(R.id.main_button_media_library, MediaLibraryActivity::class)
        setButtonNavigation(R.id.main_button_settings, SettingsActivity::class)
    }

    private fun setButtonNavigation(id: Int, cls: KClass<*>) {
        val button = findViewById<com.google.android.material.button.MaterialButton>(id)
        button.setOnClickListener {
            val intent = Intent(this, cls.java)
            startActivity(intent)
        }
    }

}