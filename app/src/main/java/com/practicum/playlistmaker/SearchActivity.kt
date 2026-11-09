package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
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
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private val updateTrackListTask by lazy {
        Runnable {
            presenter.updateTrackList(inputString)
        }
    }
    private val allowClickTask by lazy {
        Runnable { isClickAllowed = true }
    }
    private var inputString = ""
    private var isClickAllowed = true

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var placeHolder: LinearLayout
    private lateinit var placeHolderImage: ImageView
    private lateinit var placeHolderText: TextView
    private lateinit var placeHolderButton: MaterialButton
    private lateinit var searchHistoryHeader: TextView
    private lateinit var searchProgressBar: ProgressBar

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
        trackAdapter = TrackAdapter { track ->
            if (!isClickAllowed()) return@TrackAdapter
            presenter.addTrackToHistory(track)
            val intent = Intent(
                this@SearchActivity,
                AudioPlayerActivity::class.java
            ).apply { putExtra(Track.EXTRA_TRACK, track) }
            startActivity(intent)
        }
        clearButtonAdapter = ClearButtonAdapter({ presenter.clearTrackHistory() })

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

    override fun onDestroy() {
        super.onDestroy()
        cancelDebounceSearch()
        handler.removeCallbacks(allowClickTask)
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
        searchProgressBar = findViewById(R.id.search_progress_bar)

        trackRecyclerView.adapter = ConcatAdapter(trackAdapter, clearButtonAdapter)

        inputEditText.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.INVISIBLE
                    presenter.showHistory()
                } else {
                    clearButton.visibility = View.VISIBLE
                }
                inputString = s?.toString().orEmpty()
                debounceSearch()
            })

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                cancelDebounceSearch()
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
                hidePlaceHolder()
                hideProgressBar()
                hideSearchHistoryViewsElements()
                trackAdapter.updateTrackList(emptyList())
            }

            is SearchScreenState.History -> {
                hidePlaceHolder()
                hideProgressBar()
                showSearchHistoryViewsElements()
                trackAdapter.updateTrackList(screenState.trackList)
            }

            is SearchScreenState.InProgress -> {
                hidePlaceHolder()
                showProgressBar()
                hideSearchHistoryViewsElements()
                trackAdapter.updateTrackList(emptyList())
            }

            is SearchScreenState.Empty -> {
                showPlaceHolder(R.drawable.ic_search_not_found, R.string.not_found)
                hideProgressBar()
                hideSearchHistoryViewsElements()
                trackAdapter.updateTrackList(emptyList())
            }

            is SearchScreenState.Error -> {
                showPlaceHolder(R.drawable.ic_search_no_connect, R.string.no_connect, true)
                hideProgressBar()
                hideSearchHistoryViewsElements()
                trackAdapter.updateTrackList(emptyList())
            }

            is SearchScreenState.Successful -> {
                hidePlaceHolder()
                hideProgressBar()
                hideSearchHistoryViewsElements()
                trackAdapter.updateTrackList(screenState.trackList)
            }
        }
    }

    private fun hidePlaceHolder() {
        placeHolder.visibility = View.GONE
    }

    private fun showPlaceHolder(
        @DrawableRes iconId: Int, @StringRes textId: Int, visibleButton: Boolean = false
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

    private fun hideProgressBar() {
        searchProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        searchProgressBar.visibility = View.VISIBLE
    }

    private fun showSearchHistoryViewsElements() {
        searchHistoryHeader.visibility = View.VISIBLE
        clearButtonAdapter.visible = true
    }

    private fun debounceSearch() {
        cancelDebounceSearch()
        if (inputString.isEmpty()) return
        handler.postDelayed(updateTrackListTask, SEARCH_DEBOUNCE_DELAY)
    }

    private fun cancelDebounceSearch() {
        handler.removeCallbacks(updateTrackListTask)
    }

    private fun isClickAllowed(): Boolean {
        if (!isClickAllowed) return false
        isClickAllowed = false
        handler.postDelayed(allowClickTask, CLICK_DEBOUNCE_DELAY)
        return true
    }

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}