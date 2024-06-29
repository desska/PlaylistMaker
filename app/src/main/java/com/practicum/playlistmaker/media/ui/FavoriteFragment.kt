package com.practicum.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FavoriteFragmentBinding
import com.practicum.playlistmaker.media.domain.entity.FavoriteErrorType
import com.practicum.playlistmaker.media.domain.entity.FavoriteState
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.ui.PLAYER_TRACKS_KEY
import com.practicum.playlistmaker.search.ui.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModel()
    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!
    private var isClickAllowed = true
    private lateinit var favoriteAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onTrackListener = { track: Track ->
            if (clickDebounce()) {
                viewModel.onTrackClick(track)

            }
        }

        viewModel.fillData()

        favoriteAdapter = TrackAdapter(onTrackListener)
        binding.trackList.adapter = favoriteAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteState.Error -> showError(it.type)
                is FavoriteState.Content -> showContent(it.data)
                else -> {}
            }
        }

        viewModel.getTrackClickEvent().observe(viewLifecycleOwner) {
            openTrack(track = it)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openTrack(track: Track) {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra(PLAYER_TRACKS_KEY, track)
        this.startActivity(intent)

    }

    private fun showError(type: FavoriteErrorType) {
        favoriteAdapter.clear()
        binding.errorMsgImg.isVisible = true
        binding.errorMsgText.isVisible = true
        binding.errorMsgText.text = textFromErrorType(type)
    }

    private fun showContent(data: List<Track>) {
        binding.errorMsgImg.isVisible = false
        binding.errorMsgText.isVisible = false
        favoriteAdapter.setItems(data)
    }

    private fun textFromErrorType(type: FavoriteErrorType): String {
        return when (type) {
            FavoriteErrorType.EMPTY_MEDIA -> getString(R.string.empty_media_error)
        }
    }

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

    companion object {

        fun newInstance() = FavoriteFragment()
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}