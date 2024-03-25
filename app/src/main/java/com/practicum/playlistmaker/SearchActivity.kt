package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PLAYER_TRACKS_KEY = "RECENT_TRACKS"

class SearchActivity : AppCompatActivity() {

    private var searchText: String = ""
    private val itunesUrl = "https://itunes.apple.com"
    private var isClickAllowed = true

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesService::class.java)
    private lateinit var handler: Handler

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackHistoryAdapter: TrackAdapter

    private var trackList = mutableListOf<Track>()
    private var trackHistoryList = mutableListOf<Track>()

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        val sharedPrefs = getSharedPreferences(COMMON_PREFERENCE, MODE_PRIVATE)

        trackList = arrayListOf()
        trackHistoryList = SearchHistory.getListFromShared(sharedPrefs).toMutableList()

        val onTrackHistoryListener = { track: Track ->

            if (clickDebounce()) {

                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(PLAYER_TRACKS_KEY, track)
                this.startActivity(intent)
            }
        }

        trackHistoryAdapter = TrackAdapter(trackHistoryList, onTrackHistoryListener)

        val searchHistory = SearchHistory(sharedPrefs, trackHistoryAdapter)

        val onTrackListener = { track: Track ->

            if (clickDebounce()) {

                searchHistory.addTrack(track)
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(PLAYER_TRACKS_KEY, track)
                this.startActivity(intent)

            }

        }

        trackAdapter = TrackAdapter(trackList, onTrackListener)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setHistoryVisibility(false)

        binding.trackList.adapter = trackAdapter
        binding.trackListHistory.adapter = trackHistoryAdapter

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val searchButton = findViewById<ImageView>(R.id.search_edit_search_button)
        val clearButton = findViewById<ImageView>(R.id.search_edit_clear_button)

        binding.clearHistoryButton.setOnClickListener {

            searchHistory.clear()
            setHistoryVisibility(false)

        }

        clearButton.setOnClickListener {

            clearMessage()
            binding.searchUpdateButton.isVisible = false
            searchEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (trackList.isNotEmpty()) {
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
            }

            val isHistoryVisible = isHistoryVisible(
                hasFocus = true,
                isSearchTextEmpty = true,
                isHistoryListEmpty = trackHistoryAdapter.isEmpty()
            )

            setHistoryVisibility(isHistoryVisible)

        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                searchButton.isVisible = true
                clearButton.isVisible = clearButtonVisibility(s = p0)

                if (p0?.isEmpty() == true) trackAdapter.clear()

                val isHistoryVisible = isHistoryVisible(
                    searchEditText.hasFocus(),
                    p0?.isEmpty() == true,
                    trackHistoryAdapter.isEmpty()
                )
                setHistoryVisibility(isHistoryVisible)

                updateDebounce()

            }

            override fun afterTextChanged(p0: Editable?) {

                searchText = searchEditText.text.toString()

            }

        }

        searchEditText.addTextChangedListener(watcher)

        searchEditText.setOnFocusChangeListener { _, hasFocus ->

            val isHistoryVisible =
                isHistoryVisible(hasFocus, searchText.isEmpty(), trackHistoryAdapter.isEmpty())
            setHistoryVisibility(isHistoryVisible)

        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                update()

            }

            false
        }

        binding.searchUpdateButton.setOnClickListener {

            update()

        }

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_TEXT_KEY, searchText)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {

            searchText = savedInstanceState.getString(SEARCH_TEXT_KEY).toString()
            val editText = findViewById<EditText>(R.id.search_edit_text)
            editText.setText(searchText)

        }

    }

    private fun update() {

        if (searchText.isEmpty()) {

            return
        }

        clearMessage()
        setHistoryVisibility(false)
        binding.searchUpdateButton.isVisible = false
        binding.trackList.isVisible = false
        binding.progressBar.isVisible = true

        itunesService.search(searchText)
            .enqueue(object : Callback<TrackSearchResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackSearchResponse>,
                    response: Response<TrackSearchResponse>
                ) {

                    binding.progressBar.isVisible = false
                    if (response.code() == 200) {

                        trackList.clear()

                        if (response.body()?.results?.isNotEmpty() == true) {

                            binding.trackList.isVisible = true
                            trackList.addAll(response.body()?.results!!)

                        }

                        trackAdapter.notifyDataSetChanged()

                        if (trackList.isEmpty()) {

                            showMessage(
                                R.drawable.not_found_error,
                                getString(R.string.not_found_error)
                            )

                        }

                    } else {

                        showMessage(
                            R.drawable.connection_error,
                            getString(R.string.connection_error)

                        )
                        binding.searchUpdateButton.isVisible = true

                    }

                }

                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {

                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    showMessage(R.drawable.connection_error, getString(R.string.connection_error))
                    binding.searchUpdateButton.isVisible = true
                }


            })
    }


    private fun setHistoryVisibility(isVisible: Boolean) {

        binding.historyHeader.isVisible = isVisible
        binding.trackListHistory.isVisible = isVisible
        binding.clearHistoryButton.isVisible = isVisible

    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {

        return !s.isNullOrEmpty()

    }

    private companion object {
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private fun showMessage(messageRes: Int, messageTxt: String) {

        setHistoryVisibility(false)

        binding.searchMsgImg.isVisible = true
        binding.searchMsgImg.setBackgroundResource(messageRes)

        binding.searchMsgText.isVisible = true
        binding.searchMsgText.text = messageTxt

    }

    private fun clearMessage() {

        binding.searchMsgImg.isVisible = false
        binding.searchMsgText.isVisible = false

    }

    private fun isHistoryVisible(
        hasFocus: Boolean,
        isSearchTextEmpty: Boolean,
        isHistoryListEmpty: Boolean
    ) = hasFocus && isSearchTextEmpty && !isHistoryListEmpty

    private val updateRunnable = Runnable { update() }

    private fun updateDebounce() {

        handler.removeCallbacks(updateRunnable)
        handler.postDelayed(updateRunnable, SEARCH_DEBOUNCE_DELAY)

    }

    private fun clickDebounce(): Boolean {

        val current = isClickAllowed

        if (isClickAllowed) {

            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)

        }

        return current
    }

}

