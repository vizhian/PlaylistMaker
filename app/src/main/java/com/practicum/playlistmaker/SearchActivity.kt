package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SearchActivity : AppCompatActivity() {
    private lateinit var presenter: SearchActivityPresenter
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var clearButtonAdapter: ClearButtonAdapter

    private var inputString = ""

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var placeHolder: LinearLayout
    private lateinit var placeHolderImage: ImageView
    private lateinit var placeHolderText: TextView
    private lateinit var placeHolderButton: MaterialButton
    private lateinit var searchHistoryHeader: TextView

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

        val myApp = (application as MyApp)
        presenter = SearchActivityPresenter(myApp.repository, myApp.historyStorage)
        trackAdapter = TrackAdapter ( { track -> presenter.addTrackToHistory(track)})
        clearButtonAdapter = ClearButtonAdapter({presenter.clearTrackHistory()})

        initializeViews()

        presenter.screenState.observe(this, ::render)
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

    private fun initializeViews() {
        inputEditText = findViewById(R.id.input_edit_text)
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        clearButton = findViewById(R.id.clear_icon)
        placeHolder = findViewById(R.id.search_place_holder)
        placeHolderImage = findViewById(R.id.search_place_holder_ic)
        placeHolderText = findViewById(R.id.search_place_holder_text)
        placeHolderButton = findViewById(R.id.search_place_holder_update_button)
        searchHistoryHeader = findViewById(R.id.search_history_header)

        trackRecyclerView.adapter = ConcatAdapter(trackAdapter, clearButtonAdapter)

        inputEditText.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                clearButton.visibility = if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            },
            afterTextChanged = { inputString = inputEditText.text.toString() }
        )

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.updateTrackList(inputString)
                true
            } else {
                false
            }
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) presenter.showHistory()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            presenter.showHistory()
        }

        placeHolderButton.setOnClickListener { presenter.updateTrackList(inputString) }
    }

    private fun render(screenState: SearchScreenState) {
        when (screenState) {
            is SearchScreenState.Start -> {
                trackAdapter.updateTrackList(emptyList())
                hidePlaceHolder()
                hideSearchHistoryViewsElements()
            }

            is SearchScreenState.Empty -> {
                trackAdapter.updateTrackList(emptyList())
                showPlaceHolder(R.drawable.ic_search_not_found, R.string.not_found)
                hideSearchHistoryViewsElements()
            }

            is SearchScreenState.Error -> {
                trackAdapter.updateTrackList(emptyList())
                showPlaceHolder(R.drawable.ic_search_no_connect, R.string.no_connect, true)
                hideSearchHistoryViewsElements()
            }

            is SearchScreenState.Successful -> {
                trackAdapter.updateTrackList(screenState.trackList)
                hidePlaceHolder()
                hideSearchHistoryViewsElements()
            }

            is SearchScreenState.History -> {
                trackAdapter.updateTrackList(screenState.trackList)
                showSearchHistoryViewsElements()
            }
        }
    }

    private fun hidePlaceHolder() {
        placeHolder.visibility = View.GONE
    }

    private fun showPlaceHolder(
        @DrawableRes iconId: Int,
        @StringRes textId: Int,
        visibleButton: Boolean = false
    ) {
        placeHolder.visibility = View.VISIBLE
        placeHolderImage.setImageResource(iconId)
        placeHolderText.text = getString(textId)
        placeHolderButton.visibility = if (visibleButton) View.VISIBLE else View.INVISIBLE
    }

    private fun hideSearchHistoryViewsElements() {
        searchHistoryHeader.visibility = View.GONE
        clearButtonAdapter.visible = false
    }

    private fun showSearchHistoryViewsElements() {
        searchHistoryHeader.visibility = View.VISIBLE
        clearButtonAdapter.visible = true
    }

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
    }

}