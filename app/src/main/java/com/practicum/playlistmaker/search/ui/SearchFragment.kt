package com.practicum.playlistmaker.search.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.entity.ErrorType
import com.practicum.playlistmaker.search.domain.entity.SearchState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val PLAYER_TRACKS_KEY = "RECENT_TRACKS"

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()
    private var searchText: String = ""
    private var isClickAllowed = true
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackHistoryAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchEditText = binding.searchEditText
        val searchButton = binding.searchEditSearchButton
        val clearButton = binding.searchEditClearButton

        viewModel.getSearchState().observe(viewLifecycleOwner) { state ->
            showSearchState(state)
        }

        viewModel.getHistoryState().observe(viewLifecycleOwner) { state ->
            showHistoryState(state)
        }

        viewModel.getTrackClickEvent().observe(viewLifecycleOwner) {
            openTrack(track = it)
        }
        val onTrackListener = { track: Track ->
            if (clickDebounce()) {
                viewModel.onTrackClick(track)
                viewModel.addToHistory(track)
            }
        }
        trackHistoryAdapter = TrackAdapter(onTrackListener)
        trackAdapter = TrackAdapter(onTrackListener)

        binding.trackList.adapter = trackAdapter
        binding.trackListHistory.adapter = trackHistoryAdapter

        binding.clearHistoryButton.setOnClickListener {
            viewModel.onHistoryClear()
        }

        clearButton.setOnClickListener {
            clearMessage()
            binding.searchUpdateButton.isVisible = false
            searchEditText.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(Activity().currentFocus?.windowToken, 0)

            viewModel.onTrackListClear()
            val isHistoryVisible = isHistoryVisible(
                hasFocus = true,
                isSearchTextEmpty = true,
                isHistoryListEmpty = trackHistoryAdapter.isEmpty()
            )

            showHistory(isHistoryVisible)
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
                showHistory(isHistoryVisible)

                viewModel.onTextChanged(p0.toString())

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = searchEditText.text.toString()

            }

        }

        searchEditText.addTextChangedListener(watcher)

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            val isHistoryVisible =
                isHistoryVisible(hasFocus, searchText.isEmpty(), trackHistoryAdapter.isEmpty())
            showHistory(isHistoryVisible)
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onEditorActionDone(searchText)

            }

            false
        }

        binding.searchUpdateButton.setOnClickListener {
            viewModel.onEditorActionDone(searchText)
        }

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT_KEY).toString()
            binding.searchEditText.setText(searchText)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchText)
    }

    private fun openTrack(track: Track) {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra(PLAYER_TRACKS_KEY, track)
        this.startActivity(intent)

    }

    private fun showHistoryState(state: List<Track>) {
        trackHistoryAdapter.setItems(state)
        val isHistoryVisible =
            isHistoryVisible(
                binding.searchEditText.hasFocus(),
                searchText.isEmpty(),
                trackHistoryAdapter.isEmpty()
            )
        showHistory(isHistoryVisible)

    }

    private fun showHistory(isVisible: Boolean) {
        setHistoryVisibility(isVisible)
        if (isVisible) {
            clearMessage()
            binding.progressBar.isVisible = false
            binding.searchUpdateButton.isVisible = false
            binding.trackList.isVisible = false

        }
    }

    private fun showSearchState(state: SearchState) {
        setHistoryVisibility(false)

        when (state) {
            is SearchState.Content -> {
                showTrackList(state.data)
                binding.trackList.isVisible = true

            }

            is SearchState.Loading -> {
                showLoading()
            }

            is SearchState.Error -> {
                trackAdapter.clear()
                showError(state.type)

            }
        }

    }

    private fun setHistoryVisibility(isVisible: Boolean) {
        binding.historyHeader.isVisible = isVisible
        binding.trackListHistory.isVisible = isVisible
        binding.clearHistoryButton.isVisible = isVisible

    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()

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

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed

        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }

        return current
    }

    private fun showLoading() {
        clearMessage()
        setHistoryVisibility(false)
        binding.searchUpdateButton.isVisible = false
        binding.trackList.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showTrackList(tracks: List<Track>) {
        trackAdapter.setItems(tracks)
    }

    private fun showError(type: ErrorType) {
        binding.progressBar.isVisible = false
        setHistoryVisibility(false)
        val imgRes: Int
        val textRes: String

        when (type) {
            ErrorType.EMPTY -> {
                imgRes = R.drawable.not_found_error
                textRes = getString(R.string.not_found_error)

            }

            ErrorType.OTHER -> {
                imgRes = R.drawable.connection_error
                textRes = getString(R.string.connection_error)

            }
        }

        binding.searchMsgImg.isVisible = true
        binding.searchMsgImg.setBackgroundResource(imgRes)

        binding.searchMsgText.isVisible = true
        binding.searchMsgText.text = textRes

    }

    private companion object {

        const val SEARCH_TEXT_KEY = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}