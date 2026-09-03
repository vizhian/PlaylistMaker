package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private var inputString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeToolbar(R.string.search)

        inputEditText = findViewById(R.id.input_edit_text)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)

        inputEditText.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                clearButton.visibility = if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            },
            afterTextChanged = { inputString = inputEditText.text.toString() })

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val testTrackList = Track.getTestTrackList()

        val trackRecycleView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.track_recycler_view)
        trackRecycleView.adapter = TrackAdapter(testTrackList)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        inputString = savedInstanceState.getString(SEARCH_STRING) ?: ""
        if (inputString.isNotEmpty()) inputEditText.setText(inputString)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (inputString.isNotEmpty()) outState.putString(SEARCH_STRING, inputString)
    }

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
    }

}