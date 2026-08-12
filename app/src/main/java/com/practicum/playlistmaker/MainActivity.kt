package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        //После сдачи перевести на initializeClickListenerButton
        val searchButton =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.main_button_search)
        val clickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        searchButton.setOnClickListener(clickListener)

        initializeClickListenerButton(R.id.main_button_media_library) {
            val intent = Intent(this, MediaLibrary::class.java)
            startActivity(intent)
        }

        initializeClickListenerButton(R.id.main_button_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    fun initializeClickListenerButton(id: Int, fOnClick: (View?) -> Unit) {
        val button = findViewById<com.google.android.material.button.MaterialButton>(id)
        button.setOnClickListener(fOnClick)
    }

}