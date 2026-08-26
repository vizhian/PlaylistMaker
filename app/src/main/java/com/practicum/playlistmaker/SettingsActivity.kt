package com.practicum.playlistmaker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(svedInstanceState: Bundle?) {
        super.onCreate(svedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeToolbar(R.string.settings)

        val buttonShareApp =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.settings_button_share_app)
        buttonShareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_web_address))
            safelyStartActivity(Intent.createChooser(intent, getString(R.string.share_app)))
        }

        val buttonWriteToSupport =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.settings_button_write_to_support)
        buttonWriteToSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = "mailto:".toUri()
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.app_support_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_support_email_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_support_email_body))

            safelyStartActivity(intent)
        }

        val buttonUserAgreement =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.settings_button_user_agreement)
        buttonUserAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = getString(R.string.app_user_agreement).toUri()

            safelyStartActivity(intent)
        }
    }

    fun safelyStartActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.error_start_activity), Toast.LENGTH_SHORT)
                .show()
        }
    }

}